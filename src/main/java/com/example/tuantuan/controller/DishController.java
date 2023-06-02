package com.example.tuantuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tuantuan.domain.DishFlavor;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.dto.DishDto;
import com.example.tuantuan.service.Dish;
import com.example.tuantuan.service.DishFlavorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    //按照条件查询菜品口味信息
//    @GetMapping("/list")
//    public R<List<Dishee>> list(Dishee dishee){
//        Long categoryId = dishee.getCategoryId();
//        Integer status = dishee.getStatus();
//        System.out.println(categoryId);
//        LambdaQueryWrapper<Dishee> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Dishee::getCategoryId,categoryId).eq(Dishee::getStatus,status);
//        List<Dishee> list = dish.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dishee dishee){
        Long categoryId = dishee.getCategoryId();
//        Integer status = dishee.getStatus();
        LambdaQueryWrapper<Dishee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dishee::getCategoryId,categoryId).eq(Dishee::getStatus,1);
        List<Dishee> list = dish.list(queryWrapper);
        //将list集合中的数据拷贝到新的基于DishDto集合当中
        List<DishDto> collect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();//创建一个DishDto对象
            BeanUtils.copyProperties(item, dishDto);//将集合中各元素的数据拷贝到DishDto对象中
            //获取该菜品的口味数据
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            //将口味数据封装到dishDto对象中
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }
}
