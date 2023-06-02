package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.Setmealee;
import com.example.tuantuan.dto.SetmealDto;
import com.example.tuantuan.service.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//套餐管理
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private Setmeal setmeal;

    //添加
    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto) {
        setmeal.addWithDish(setmealDto);
        return R.success("添加成功");
    }

    //分页查询
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {
        return setmeal.page(page,pageSize,name);
    }

    //根据id查询套餐信息
    @GetMapping("/{id}")
    public R<SetmealDto> getByIdWithDish(@PathVariable Long id){
        return setmeal.getByIdWithDish(id);
    }
    //修改
    @PutMapping
    public R<String> updateWithDish(@RequestBody SetmealDto setmealDto){
        setmeal.updateWithDish(setmealDto);
        return R.success("修改成功");
    }
    //停售
    @PostMapping("/status/0")
    public R<String> stopSales(Long []ids){
        for (Long id : ids) {
            setmeal.stopSales(id);
        }
        return R.success("操作成功");
    }
    //起售
    @PostMapping("/status/1")
    public R<String> startSales(Long []ids){
        for (Long id : ids) {
            setmeal.startSales(id);
        }
        return R.success("操作成功");
    }
    //删除
    @DeleteMapping
    public R<String> deleteById(Long []ids){
        for (Long id : ids) {
            setmeal.deleteById(id);
        }
        return R.success("删除成功");
    }
    //根据条件查询套餐信息
    @GetMapping("/list")
    public R<List<Setmealee>> list(Setmealee setmealee){
        Long categoryId = setmealee.getCategoryId();
//        Integer status = setmealee.getStatus();
        LambdaQueryWrapper<Setmealee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmealee::getCategoryId,categoryId).eq(Setmealee::getStatus,1);
        List<Setmealee> list = setmeal.list(queryWrapper);
        return R.success(list);
    }
}
