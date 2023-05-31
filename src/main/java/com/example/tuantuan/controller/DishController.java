package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.DishFlavor;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.dto.DishDto;
import com.example.tuantuan.service.Dish;
import com.example.tuantuan.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private Dish dish;
    @Autowired
    private DishFlavorService dishFlavorService;

    //分页查询菜品
    @GetMapping("page")
    public R<Page> page(int page, int pageSize, String name) {
        return dish.page(page, pageSize, name);
    }

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dish.saveWithFlavor(dishDto);
        return R.success("添加成功！");
    }

    //通过id查询菜品信息
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        return dish.getByIdWithFlavor(id);
    }

    //修改
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dish.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    //删除
    @DeleteMapping
    public R<String> deleteByIdWithFlavor(Long [] ids) {
        for (Long id : ids) {
            //删除菜品表中的数据
            dish.removeById(id);
            //删除口味表中的数据
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
        }
        return R.success("删除成功");
    }

    //停售
    @PostMapping("/status/0")
    public R<String> stopSales(Long [] ids) {
        for (Long id : ids) {
            dish.stopSales(id);
        }
        return R.success("操作成功");
    }

    //起售
    @PostMapping("/status/1")
    public R<String> startSales(Long [] ids) {
        for (Long id : ids) {
            dish.startSales(id);
        }
        return R.success("操作成功");
    }
    //按照分类id查询菜品
    @GetMapping("/list")
    public R<List<Dishee>> list(Long categoryId){
        System.out.println(categoryId);
        LambdaQueryWrapper<Dishee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dishee::getCategoryId,categoryId);
        List<Dishee> list = dish.list(queryWrapper);
        return R.success(list);
    }
}
