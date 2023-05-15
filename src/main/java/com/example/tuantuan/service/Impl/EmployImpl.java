package com.example.tuantuan.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.EmployMapper;
import com.example.tuantuan.domain.Employee;
import com.example.tuantuan.service.Employ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;


@Service
public class EmployImpl extends ServiceImpl<EmployMapper,Employee> implements Employ {
    @Autowired
    private EmployMapper employMapper;
//
//    @Override
    public String encryption(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
//
    @Override
    public Employee login(String username) {
        Employee login = employMapper.login(username);
        return login;
    }

    @Override
    public void save(Employee employee, HttpServletRequest httpServletRequest) {
//        给新添加用户设置默认密码
        employee.setPassword(encryption("123456"));
//        给新用户添加create_user和update_user信息,获取保存在Session中的登录账号信息
        Long id = (Long) httpServletRequest.getSession().getAttribute("employ");
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        employMapper.save(employee);
    }

}
