package com.atguigu.zhihuixiaoyuan.controller;

import com.atguigu.zhihuixiaoyuan.pojo.Admin;
import com.atguigu.zhihuixiaoyuan.service.AdminService;
import com.atguigu.zhihuixiaoyuan.util.MD5;
import com.atguigu.zhihuixiaoyuan.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/getAllAdmin/{pageNum}/{pageSize}")
    public Result getAllAdmin(@PathVariable Integer pageNum, @PathVariable Integer pageSize, String adminName){
        Page<Admin> pageInfo = new Page<>(pageNum,pageSize);
        IPage<Admin> ipage = adminService.getAdminsByOpr(pageInfo, adminName);
        return Result.ok(ipage);
    }




    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        Integer id = admin.getId();
        if (id==null||id==0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }




    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
