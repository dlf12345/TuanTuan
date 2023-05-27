package com.example.tuantuan.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data//编译时自动添加成员方法
@Component//将该类的创建对象控制权交给IOC容器
//使用ConfigurationProperties注解，可以将配置文件中的数据注入到该类的bean对象中
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSS {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
