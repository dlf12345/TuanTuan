package com.example.tuantuan.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.DishMapper;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.service.Dish;
import org.springframework.stereotype.Service;

@Service
public class DishImpl extends ServiceImpl<DishMapper, Dishee> implements Dish{

}
