package com.atguigu.zhihuixiaoyuan.controller;

import com.atguigu.zhihuixiaoyuan.pojo.Clazz;
import com.atguigu.zhihuixiaoyuan.service.ClazzService;
import com.atguigu.zhihuixiaoyuan.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags="班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {


    @Autowired
    private ClazzService clazzService;



    @ApiOperation("获取所有班级的JSON")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzList = clazzService.getClazzs();
        return Result.ok(clazzList);
    }



    @ApiOperation("删除一个或者多个班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazzByIds(@ApiParam("多个班级id的JSON")@RequestBody List<Integer> ids){
        clazzService.removeByIds(ids);
        return Result.ok();

    }




    @ApiOperation("增加或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("JSON格式的班级信息")@RequestBody Clazz clazz){

        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }





    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询页码") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小")@PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件")Clazz clazz

    ){
        Page<Clazz> page =new Page<>(pageNo,pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page, clazz);
        return Result.ok(iPage);

    }
}
