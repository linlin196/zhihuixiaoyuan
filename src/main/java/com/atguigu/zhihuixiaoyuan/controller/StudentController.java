package com.atguigu.zhihuixiaoyuan.controller;


import com.atguigu.zhihuixiaoyuan.pojo.Student;
import com.atguigu.zhihuixiaoyuan.service.StudentService;
import com.atguigu.zhihuixiaoyuan.util.MD5;
import com.atguigu.zhihuixiaoyuan.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;



    @ApiOperation("删除一个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("多个学生id的JSON") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }





    @ApiOperation("增加或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("要增加或者修改学生的json")@RequestBody Student student){
        Integer id = student.getId();
        if (null==id||0==id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }

        studentService.saveOrUpdate(student);
        return Result.ok();

    }





    @ApiOperation("查询学生信息,分页带条件")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentsByOpr(
            @ApiParam("页码数") @PathVariable("pageNo")Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize")Integer pageSize,
            @ApiParam("查询条件转换后端数据模型") Student student
    ){
        // 准备分页信息封装的page对象
        Page<Student> page =new Page<>(pageNo,pageSize);
        // 获取分页的学生信息
        IPage<Student> iPage = studentService.getStudentByOpr(page, student);
        // 返回学生信息
        return Result.ok(iPage);

    }


}
