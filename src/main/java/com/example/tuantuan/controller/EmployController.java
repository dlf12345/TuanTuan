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
        String afterPassword = employImpl.encryption(password);
//        根据username获取用户信息
        Employee emp = employImpl.login(username);
        int status = emp.getStatus();//获取用户状态码，1为可用2为禁用
        if (status == 1) {
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
        return R.error("用户已被禁用");
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
    public R<String> save(@RequestBody Employee employee,HttpServletRequest httpServletRequest) {

        employImpl.save(employee,httpServletRequest);
        return R.success("添加成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name)
                .orderByDesc(Employee::getUpdateTime);

        //执行查询
        employImpl.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        System.out.println(employee);
        employImpl.update(httpServletRequest,employee);
        return R.success("修改用户状态成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee byId = employImpl.getById(id);
        return R.success(byId);
    }
}
