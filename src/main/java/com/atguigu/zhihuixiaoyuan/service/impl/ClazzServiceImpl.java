package com.atguigu.zhihuixiaoyuan.service.impl;


import com.atguigu.zhihuixiaoyuan.mapper.ClazzMapper;
import com.atguigu.zhihuixiaoyuan.pojo.Clazz;
import com.atguigu.zhihuixiaoyuan.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {


    @Override
    public IPage<Clazz> getClazzsByOpr(Page<Clazz> page, Clazz clazz) {

        QueryWrapper queryWrapper = new QueryWrapper();
        String gradeName = clazz.getGradeName();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }

        String name = clazz.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }

        queryWrapper.orderByDesc("id");

        Page page1 = baseMapper.selectPage(page, queryWrapper);

        return page1;

    }

    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}
