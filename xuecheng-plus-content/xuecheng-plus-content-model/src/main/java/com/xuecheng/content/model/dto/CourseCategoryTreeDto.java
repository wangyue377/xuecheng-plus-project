package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 课程分类树型结点dto
 * @author Mr.M
 * @date 2022/9/7 15:16
 * @version 1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
  //CourseCategory（继承） + childrenTreeNodes = CourseCategoryTreeDto
  //子节点
  List<CourseCategoryTreeDto> childrenTreeNodes;
}
