package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.Setmealee;
import com.example.tuantuan.dto.SetmealDto;

import java.util.List;

public interface  Setmeal extends IService<Setmealee> {
    //添加套餐
    void addWithDish(SetmealDto setmealDto);
    //分页查询
    R<Page> page(int page, int pageSize, String name);
    //根据id查询套餐信息
    R<SetmealDto> getByIdWithDish(Long id);
    //修改套餐信息
    void updateWithDish(SetmealDto setmealDto);
    //停售
    void stopSales(Long id);
    //起售
    void startSales(Long id);
}
