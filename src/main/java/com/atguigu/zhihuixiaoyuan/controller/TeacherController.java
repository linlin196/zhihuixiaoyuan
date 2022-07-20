package com.atguigu.zhihuixiaoyuan.controller;


import com.atguigu.zhihuixiaoyuan.pojo.Teacher;
import com.atguigu.zhihuixiaoyuan.service.TeacherService;
import com.atguigu.zhihuixiaoyuan.util.MD5;
import com.atguigu.zhihuixiaoyuan.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;




    @GetMapping("/getTeachers/{pageNum}/{pageSize}")
    public Result getTeachers(@PathVariable Integer pageNum, @PathVariable Integer pageSize, Teacher teacher) {
        Page<Teacher> pageParam = new Page<>(pageNum, pageSize);
        IPage<Teacher> page = teacherService.getTeachersByOpr(pageParam, teacher);
        return Result.ok(page);
    }



    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher) {
        Integer id = teacher.getId();
        if (id==null||id==0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
