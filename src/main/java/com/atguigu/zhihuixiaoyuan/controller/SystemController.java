package com.atguigu.zhihuixiaoyuan.controller;


import com.atguigu.zhihuixiaoyuan.pojo.Admin;
import com.atguigu.zhihuixiaoyuan.pojo.LoginForm;
import com.atguigu.zhihuixiaoyuan.pojo.Student;
import com.atguigu.zhihuixiaoyuan.pojo.Teacher;
import com.atguigu.zhihuixiaoyuan.service.AdminService;
import com.atguigu.zhihuixiaoyuan.service.StudentService;
import com.atguigu.zhihuixiaoyuan.service.TeacherService;
import com.atguigu.zhihuixiaoyuan.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;




    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,@PathVariable("oldPwd") String oldPwd,@PathVariable("newPwd") String newPwd){
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            //token过期
            return Result.fail().message("token失效!请重新登录后修改密码");
        }
        //通过token获取当前登录的用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd=MD5.encrypt(newPwd);

        switch(userType){
            case 1:
                QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
                queryWrapper.eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper);
                if(admin!=null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue()).eq("password",oldPwd);
                queryWrapper2.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if(student!=null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
                break;

            case 3:
                QueryWrapper<Teacher> queryWrapper3=new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue()).eq("password",oldPwd);
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if(teacher!=null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
                break;

        }
        return Result.ok();

    }







    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("文件二进制数据")@RequestPart("multipartFile") MultipartFile multipartFile,HttpServletRequest request
            ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName=uuid+originalFilename.substring(i); //uuid.jpg

        //保存文件,将文件发送到第三方、独立图片服务器上
        String portraitPath="D:/Java/learning/zhihuixiaoyuan/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //响应图片路径
        String path="upload/".concat(newFileName);
        return Result.ok(path);

    }




    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null,ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);


        Map<String,Object> map=new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin=adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student=studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher=teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }


        return Result.ok(map);

    }




    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        String sessionverifiCode = (String)session.getAttribute("verifiCode");
        String loginverifiCode = loginForm.getVerifiCode();
        if("".equals(sessionverifiCode)||null==sessionverifiCode){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if(!sessionverifiCode.equalsIgnoreCase(loginverifiCode)){
            return Result.fail().message("有误，请小心输入后重试");
        }
        //从session域中移除验证码
        session.removeAttribute("verifiCode");

        //分用户类型进行校验
        //准备一个map用于存放响应的数据
        Map<String,Object> map=new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin=adminService.login(loginForm);
                    if(null!=admin){
                        //用户类型和用户id转换为密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student=studentService.login(loginForm);
                    if(null!=student){
                        //用户类型和用户id转换为密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher=teacherService.login(loginForm);
                    if(null!=teacher){
                        //用户类型和用户id转换为密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }

        return Result.fail().message("查无此用户");

    }






    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上验证码
        String verifiCode=new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);

        //将验证码图片响应给浏览器

        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
