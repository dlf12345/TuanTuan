package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.Categoryee;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.service.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private Dish dish;
    @GetMapping("page")
    public R<Page> page(int page,int pageSize){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Dishee> queryWrapper = new LambdaQueryWrapper();

        //添加排序条件，根据sort进行排序
//        queryWrapper.orderByAsc(Categoryee::getCreateTime);

        //执行查询
        dish.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

}
