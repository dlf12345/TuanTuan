package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.Categoryee;

public interface Category extends IService<Categoryee> {
    public int deleteById(Long ids);

}
