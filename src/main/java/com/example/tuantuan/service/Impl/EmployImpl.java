package com.example.tuantuan.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.EmployMapper;
import com.example.tuantuan.domain.Employee;
import com.example.tuantuan.service.Employ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;



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
        employee.setPassword(encryption("123456"));
        employMapper.save(employee);
    }

//    @Override
//    public void save(Employee employee) {
////        给新添加用户设置默认密码
//        employee.setPassword(encryption("123456"));
////        给新用户添加create_user和update_user信息,获取保存在Session中的登录账号信息
////        Long id = (Long) httpServletRequest.getSession().getAttribute("employ");
////        employee.setCreateUser(id);
////        employee.setUpdateUser(id);
//        employMapper.save(employee);
//    }

    @Override
    public int update(HttpServletRequest httpServletRequest, Employee employee) {
        //获得当前操作用户id
        Long id = (Long)httpServletRequest.getSession().getAttribute("employ");
//        设置执行当前操作的用户id
        employee.setUpdateUser(id);
        //设置当前修改时间
        employee.setUpdateTime(LocalDateTime.now());
        int count = employMapper.updateById(employee);
        return count;
    }

}
