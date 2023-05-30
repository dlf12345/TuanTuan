package com.example.tuantuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//servlet组件扫描注解，添加此注解才能使用过滤器
@ServletComponentScan
@SpringBootApplication
//开启事务注解
@EnableTransactionManagement
public class TuanTuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuanTuanApplication.class, args);
    }

}
