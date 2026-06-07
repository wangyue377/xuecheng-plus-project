package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/*课程计划管理相关接口*/
public interface TeachplanService {
    /*根据课程id查询课程计划*/
    public List<TeachplanDto> findTeachplanTree(Long courseId);

    /*新增或修改课程计划*/
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /*删除课程计划*/
    public void deleteTeachplan(Long teachplanId);

    /*章节下调*/
    public void movedown(Long teachplanId);

    /*章节上调*/
    public void moveup(Long teachplanId);

    /**
     * @description 教学计划绑定媒资
     * @param bindTeachplanMediaDto
     * @return com.xuecheng.content.model.po.TeachplanMedia
     * @author Mr.M
     * @date 2022/9/14 22:20
     */
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

}
