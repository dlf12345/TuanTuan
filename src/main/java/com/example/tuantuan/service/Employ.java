package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.Employee;

import javax.servlet.http.HttpServletRequest;


public interface Employ  extends IService<Employee>{
//    //对后台管理系统用户输入密码进行md5加密
    public String encryption(String password);
//    //后台管理系统用户登录
    public Employee login(String username);
    public void save(Employee employee, HttpServletRequest httpServletRequest);
}
