package com.atguigu.zhihuixiaoyuan.service;


import com.atguigu.zhihuixiaoyuan.pojo.LoginForm;
import com.atguigu.zhihuixiaoyuan.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface TeacherService extends IService<Teacher> {


    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher);
}
