package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.Employee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.service.Employ;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

//添加注解，定义当前类为请求响应类
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployController {
    @Autowired
    private Employ employImpl;






//    登录功能接口
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {//接收前端传入的json格式的数据
        String username = employee.getUsername();
        String password = employee.getPassword();//获取前端请求中的password参数
//        数据库中的用户密码是通过md5加密后保存的，所以在比对之前要对获取到的密码进行加密处理
//        String afterPassword = employImpl.encryption(password);
        String afterPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据usrname获取到用户信息
//        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employImpl.login(username);
//        登录失败
        if (emp == null) {
            return R.error("用户不存在");
        }
        if (!afterPassword.equals(emp.getPassword())) {
            return R.error("密码输入错误");
        }
        if (emp.getStatus() == 0) {
            return R.error("本用户已被禁用");
        }
//        登录成功
        //将本用户的id保存到Session中，用于接下来的会话跟踪和登录校验
        httpServletRequest.getSession().setAttribute("employ", emp.getId());
//        将用户信息封装起来并以json格式返回给前端
        return R.success(emp);
    }


//    退出功能接口
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest) {
//        (1)清除Session中保存的用户id
        httpServletRequest.getSession().removeAttribute("employ");
//        (2)给前端返回一个退出成功的标识
        return R.success("退出成功");
    }



//    添加用户功能接口
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest httpServletRequest){
        employImpl.save(employee,httpServletRequest);
        return R.success("添加成功");
    }






    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employImpl.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

}
