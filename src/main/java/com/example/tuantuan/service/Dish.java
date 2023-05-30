package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.dto.DishDto;

public interface Dish extends IService<Dishee> {
    void saveWithFlavor(DishDto dishDto);
    R<Page> page(int page,int pageSize,String name);
    R<DishDto> getByIdWithFlavor(Long id);
    void updateWithFlavor(DishDto dishDto);
    //停售
    void stopSales(Long ids);
    //起售
    void startSales(Long ids);
}
