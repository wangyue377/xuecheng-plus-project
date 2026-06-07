package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频处理任务
 * @author xuxueli 2019-12-11 21:52:51
 */
@Component
@Slf4j
public class VideoTask {
    @Autowired
    MediaFileProcessService mediaFileProcessService;

    @Autowired
    MediaFileService mediaFileService;

    //ffmpeg的路径
    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegpath;

    /**
     * 2、分片广播任务
     */
    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();   //执行器的序号
        int shardTotal = XxlJobHelper.getShardTotal();   //执行器总数

        // 确定cpu的核数
        int processors = Runtime.getRuntime().availableProcessors();

        // 查询待处理任务
        List<MediaProcess> mediaProcessList = mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, processors);

        // 任务数量
        int size = mediaProcessList.size();
        log.debug("取到的视频处理任务数："+size);
        if(size<=0){
            return;
        }

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        //使用一个计数器 - 是为了让所有线程把任务执行完才结束
        //因为线程池开启之后，这个handler方法就结束了，而线程里的任务还在执行，因此阻塞一下，等所有的都执行结束
        CountDownLatch countDownLatch = new CountDownLatch((size));
        mediaProcessList.forEach(mediaProcess -> {
            //将任务加入线程池
            executorService.execute(() -> {
                try {
                    // 任务id
                    Long taskId = mediaProcess.getId();
                    // 开启任务
                    boolean b = mediaFileProcessService.startTask(taskId);
                    if(!b){
                        log.debug("抢占任务失败，任务id:{}", taskId);
                        return;
                    }

                    //文件的id就是md5值
                    String fileId = mediaProcess.getFileId();

                    //桶
                    String bucket = mediaProcess.getBucket();
                    String objectName = mediaProcess.getFilePath();
                    //下载minio视频到本地
                    File file = mediaFileService.downloadFileFromMinIO(bucket, objectName);
                    if(file == null){
                        log.debug("下载视频出错，任务id:{},bucket:{},object:{}",taskId,bucket,objectName);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "下载视频到本地失败");
                        return;
                    }

                    //源avi视频的路径
                    String video_path = file.getAbsolutePath();
                    //转换后mp4文件的名称
                    String mp4_name = fileId + "mp4";
                    //转换后mp4文件的路径
                    //先创建一个临时文件，作为转换后的文件
                    File mp4File = null;
                    try {
                        mp4File = File.createTempFile("minio", ".mp4");
                    } catch (IOException e) {
                        log.debug("创建临时文件异常,{}", e.getMessage());
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建临时文件异常");
                        return;
                    }
                    String mp4_path = mp4File.getAbsolutePath();
                    //创建工具类对象
                    Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegpath,video_path,mp4_name,mp4_path);
                    //开始视频转换，成功将返回success
                    String result = videoUtil.generateMp4();
                    if(!result.equals("success")){
                        log.debug("视频转码失败，原因:{},bucket:{},objectName:{}", result, bucket, objectName);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                        return;
                    }
                    // 上传到minio
                    boolean b1 = mediaFileService.addMediaFilesToMinIO(mp4File.getAbsolutePath(), "video/mp4", bucket, objectName);
                    if(!b1){
                        log.debug("上传mp4到minio失败，taskId:{}", taskId);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "上传mp4到minio失败");
                    }

                    //mp4文件的url
                    String filePath = getFilePathByMd5(fileId, ".mp4");

                    //任务状态为成功
                    // 保存任务的处理结果
                    mediaFileProcessService.saveProcessFinishStatus(taskId, "2", fileId, filePath, null);
                } finally {
                    //计数器减1
                    countDownLatch.countDown();
                }
            });
        });

        //阻塞，指定最大限制的等待时间，阻塞最多等待一段时间后就解除阻塞
        countDownLatch.await(30, TimeUnit.MINUTES);

    }


    /**
     * 得到合并后的文件的地址
     * @param fileMd5 文件id即md5值
     * @param fileExt 文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5,String fileExt){
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }




}
