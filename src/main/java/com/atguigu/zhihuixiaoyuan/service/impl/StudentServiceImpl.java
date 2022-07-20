package com.atguigu.zhihuixiaoyuan.service.impl;


import com.atguigu.zhihuixiaoyuan.mapper.StudentMapper;
import com.atguigu.zhihuixiaoyuan.pojo.Admin;
import com.atguigu.zhihuixiaoyuan.pojo.LoginForm;
import com.atguigu.zhihuixiaoyuan.pojo.Student;
import com.atguigu.zhihuixiaoyuan.service.StudentService;
import com.atguigu.zhihuixiaoyuan.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**

 */
@Service("stuServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student=baseMapper.selectOne(queryWrapper);
        return student;

    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(student.getName())){
            queryWrapper.like("name", student.getName());
        }
        if(!StringUtils.isEmpty(student.getClazzName())){
            queryWrapper.like("clazz_name", student.getClazzName());
        }
        queryWrapper.orderByDesc("id");
        IPage<Student> pages = baseMapper.selectPage(page, queryWrapper);
        return pages;
    }
}
