package com.atguigu.zhihuixiaoyuan.service.impl;

import com.atguigu.zhihuixiaoyuan.mapper.AdminMapper;
import com.atguigu.zhihuixiaoyuan.pojo.Admin;
import com.atguigu.zhihuixiaoyuan.pojo.LoginForm;
import com.atguigu.zhihuixiaoyuan.service.AdminService;
import com.atguigu.zhihuixiaoyuan.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("adServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {


    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Admin admin=baseMapper.selectOne(queryWrapper);
        return admin;
    }



    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Admin> getAdminsByOpr(Page<Admin> pageInfo, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(adminName)){
            queryWrapper.like("name",adminName);
        }
        queryWrapper.orderByDesc("id");

        Page page = baseMapper.selectPage(pageInfo, queryWrapper);
        return page;
    }


}
