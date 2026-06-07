package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getCourseTeacher(Long courseId) {
        return courseTeacherMapper.selectByCourseId(courseId);
    }

    /*新增或修改教师*/
    @Override
    public CourseTeacher updateCourseTeacher(CourseTeacher courseTeacher) {
        Long id = courseTeacher.getId();
        if(id == null){
            //新增
            courseTeacherMapper.insert(courseTeacher);
        }else{
            //修改
            courseTeacherMapper.updateById(courseTeacher);
        }

        return null;
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long id) {
        courseTeacherMapper.deleteByCourseIdAndId(courseId, id);
    }


}
