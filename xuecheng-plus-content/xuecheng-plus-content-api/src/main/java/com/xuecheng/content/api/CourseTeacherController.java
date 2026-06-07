package com.xuecheng.content.api;


import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;
    @Autowired
    CourseBaseMapper courseBaseMapper;


    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacher(@PathVariable Long courseId){
        long companyId = 1232141425L;
        //根据课程id查机构id
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        Long companyId1 = courseBase.getCompanyId();
        if(companyId1 != companyId){
            XueChengPlusException.cast("只能查本机构的教师信息");
        }
        return courseTeacherService.getCourseTeacher(courseId);
    }

    @PostMapping("/courseTeacher")
    public CourseTeacher addCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        long companyId = 1232141425L;
        //根据课程id查机构id
        CourseBase courseBase = courseBaseMapper.selectById(courseTeacher.getCourseId());
        Long companyId1 = courseBase.getCompanyId();
        if(companyId1 != companyId){
            XueChengPlusException.cast("只能添加本机构课程的教师信息");
        }
        return courseTeacherService.updateCourseTeacher(courseTeacher);
    }

    @PutMapping("/courseTeacher")
    public CourseTeacher updateCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        long companyId = 1232141425L;
        //根据课程id查机构id
        CourseBase courseBase = courseBaseMapper.selectById(courseTeacher.getCourseId());
        Long companyId1 = courseBase.getCompanyId();
        if(companyId1 != companyId){
            XueChengPlusException.cast("只能修改本机构课程的教师信息");
        }
        return courseTeacherService.updateCourseTeacher(courseTeacher);
    }

    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long id){
        courseTeacherService.deleteCourseTeacher(courseId, id);
    }
}
