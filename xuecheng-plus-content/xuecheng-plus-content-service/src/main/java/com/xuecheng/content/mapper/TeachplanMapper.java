package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    //课程计划查询
    public List<TeachplanDto> selectTreeNodes(long courseId);

    //章下面的小节数
    public int countSection(long teachplanId);

    //章节下调
    public void movedown(long teachplanId);

    //章节上调
    public void moveup(long teachplanId);

    /*按照课程id删除课程计划*/
    void deleteByCourseId(long courseId);
}
