package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {
    /*根据课程Id查教师*/
    public List<CourseTeacher> getCourseTeacher(Long courseId);

    /*添加或修改教师*/
    public CourseTeacher updateCourseTeacher(CourseTeacher courseTeacher);

    /*删除教师*/
    public void deleteCourseTeacher(Long courseId, Long id);

}
