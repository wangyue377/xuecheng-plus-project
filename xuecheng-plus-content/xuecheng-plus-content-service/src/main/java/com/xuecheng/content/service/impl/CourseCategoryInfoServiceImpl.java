package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseCategoryInfoServiceImpl implements CourseCategoryService {
    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //1.调用mapper查询出分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        //2.找到每个节点的子节点，最终封装成List<CourseCategoryTreeDto>类型
        //2.1.先将list转成map，key就是结点的id，value就是CourseCategoryTreeDto对象，目的就是为了方便从map获取结点
        //toMap第三个参数是如果key重复了以哪个为主
        Map<String, CourseCategoryTreeDto> collect = courseCategoryTreeDtos.stream()
                .filter(item->!id.equals(item.getId()))  //过滤掉根节点
                .collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        //2.2.从头遍历List<CourseCategoryTreeDto>，一边遍历一边找子节点放在父节点的childrenTreeNodes
        //定义一个list，作为最终返回的list
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item ->{
            //向list写入元素 - 第一层 - 1-1, 1-10
            if(item.getParentid().equals(id)){
                courseCategoryList.add(item);
            }
            //找到节点的父节点 - 第二层、第三层乃至第n层都可以
            CourseCategoryTreeDto courseCategoryParent = collect.get(item.getParentid());
            if(courseCategoryParent!=null){  //1-1和1-10的父节点为空，因为被过滤了
                if(courseCategoryParent.getChildrenTreeNodes() == null){
                    //如果该父节点的childrenTreeNodes属性为空要new一个集合，并向该集合中放子节点
                    courseCategoryParent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                //到每个节点的子节点放在父节点的childrenTreeNodes
                courseCategoryParent.getChildrenTreeNodes().add(item);
            }



        });

        return courseCategoryList;
    }
}
