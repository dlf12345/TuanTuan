package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.Categoryee;
import com.example.tuantuan.domain.Employee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.service.Category;
import com.example.tuantuan.service.Impl.CategoryImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private Category categoryImpl;
    //新增类别
    @PostMapping
    public R<String> save(@RequestBody Categoryee categoryee){
        //调用mybatisplus封装的save方法进行保存
        categoryImpl.save(categoryee);
        return R.success("保存成功");
    }


    //分页查询前端展示
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Categoryee> queryWrapper = new LambdaQueryWrapper();

        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Categoryee::getSort);

        //执行查询
        categoryImpl.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    //根据id删除
    @DeleteMapping
    public R<String> deleteById(Long ids){
        categoryImpl.deleteById(ids);
        return R.success("删除成功");
    }

    //修改
    @PutMapping
    public R<String> update(@RequestBody Categoryee categoryee){
       categoryImpl.updateById(categoryee);
       return R.success("修改分类信息成功");
    }
}

