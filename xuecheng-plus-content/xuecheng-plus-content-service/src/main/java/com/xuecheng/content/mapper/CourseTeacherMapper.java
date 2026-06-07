package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {

    //根据课程id查授课教师
    public List<CourseTeacher> selectByCourseId(long courseId);

    //根据课程id和教师id删除教师信息
    public void deleteByCourseIdAndId(@Param("course_id") long courseId, @Param("id") long id);

    //根据课程id删除教师信息
    void deleteByCourseId(Long courseId);
}
