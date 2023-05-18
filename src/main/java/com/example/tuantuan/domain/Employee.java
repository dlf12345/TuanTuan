package com.example.tuantuan.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
//Employ实体类，用于封装数据库数据
@Data
//设置实体类所对应的数据表
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
//    将属性对应的字段设置为主键
//    使用type属性设置id的生成策略为自增
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
