package com.atguigu.zhihuixiaoyuan.service;

import com.atguigu.zhihuixiaoyuan.pojo.Admin;
import com.atguigu.zhihuixiaoyuan.pojo.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    IPage<Admin> getAdminsByOpr(Page<Admin> pageInfo, String adminName);
}
