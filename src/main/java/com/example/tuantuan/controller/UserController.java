package com.example.tuantuan.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.User;
import com.example.tuantuan.service.UserService;
import com.example.tuantuan.utils.SMSUtils;
import com.example.tuantuan.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //调用工具类生成一个验证码
        if(StringUtils.isNotEmpty(phone)){
            String s = ValidateCodeUtils.generateValidateCode(6).toString();
            System.out.println(s);
//            使用阿里云SMS服务发送验证码短信
            SMSUtils.sendMessage("TuanTuan","SMS_461075379",phone,s);
//            将生成的验证码保存在session中，用于登录的时候进行比对
            session.setAttribute(phone,s);
            return R.success("短信发送成功");
        }
        return R.error("验证码生成错误");

    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession!=null&&codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
//            //将当前登录用户的id保存在Session中用户登录校验
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");

    }

}
