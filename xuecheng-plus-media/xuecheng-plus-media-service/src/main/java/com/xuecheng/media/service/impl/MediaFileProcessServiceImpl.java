package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/9/14 14:41
 * @version 1.0
 */
@Slf4j
@Service
public class MediaFileProcessServiceImpl implements MediaFileProcessService {

 @Autowired
 MediaFilesMapper mediaFilesMapper;

 @Autowired
 MediaProcessMapper mediaProcessMapper;

 @Autowired
 MediaProcessHistoryMapper mediaProcessHistoryMapper;


 @Override
 public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
  List<MediaProcess> mediaProcesses = mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
   return mediaProcesses;
 }

 //实现如下
 public boolean startTask(long id) {
  int result = mediaProcessMapper.startTask(id);
  return result<=0?false:true;
 }

 @Override
 public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
  //要更新的任务
  MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
  if(mediaProcess == null){
   return;
  }
  //如果任务执行失败
  if(status.equals("3")){
   //更新media_process表的状态
   mediaProcess.setStatus("3");
   mediaProcess.setFailCount(mediaProcess.getFailCount() + 1);
   mediaProcess.setErrormsg(errorMsg);
   mediaProcessMapper.updateById(mediaProcess);
   log.debug("更新任务处理状态为失败，任务信息:{}",mediaProcess);
   return;
   //更高效的更新
   // mediaProcessMapper.update()
//   mediaProcessMapper.update(null, new LambdaUpdateWrapper<MediaProcess>()
//           .set(MediaProcess::getStatus, "3")
//           .setSql("fail_count = fail_count + 1") // 直接在数据库层面累加，防止并发覆盖
//           .set(MediaProcess::getErrormsg, errorMsg)
//           .eq(MediaProcess::getId, mediaProcess.getId()) // 这里的 ID 必须指定
//   );
  }

  //===================== 如果任务执行成功 ==========================
  //文件表记录
  MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
  //更新media_file表中的url - 为什么要更新这个？ - 更新成.mp4后缀
  mediaFiles.setUrl(url);
  mediaFilesMapper.updateById(mediaFiles);

  //更新media_process表的状态
  mediaProcess.setStatus("2");
  mediaProcess.setFinishDate(LocalDateTime.now());
  mediaProcess.setUrl(url);
  mediaProcessMapper.updateById(mediaProcess);

  //将media_process表记录插入到media_process_history表
  MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
  BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
  mediaProcessHistoryMapper.insert(mediaProcessHistory);

  //从media_process删除当前任务
  mediaProcessMapper.deleteById(taskId);
 }


}