package com.example.tuantuan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.DishMapper;
import com.example.tuantuan.domain.Categoryee;
import com.example.tuantuan.domain.DishFlavor;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.dto.DishDto;
import com.example.tuantuan.service.Category;
import com.example.tuantuan.service.Dish;
import com.example.tuantuan.service.DishFlavorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishImpl extends ServiceImpl<DishMapper, Dishee> implements Dish {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    //使用@Lazy注解，延迟加载Category类型的bean对象，解决依赖循环问题
    //依赖循环，Dish中依赖了Category，Category中依赖了Dish
    @Lazy
    private Category category;
    @Autowired
    private DishMapper dishMapper;


    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表中
        this.save(dishDto);
        //获取菜品id
        Long id = dishDto.getId();
        //菜品口味对象集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历口味集合，为里面的口味对象设置菜品id
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        //添加菜品口味对象，按照集合的方式整体添加
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dishee> pageInfo = new Page<>(page, pageSize);
        //创建一个新的基于DishDto的page对象，因为基于Dishee的page对象中没有菜品名称，不符合前端的需求
        Page<DishDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dishee> queryWrapper = new LambdaQueryWrapper();
        //添加查询条件，模糊查询
        queryWrapper.like(name != null, Dishee::getName, name);
        //添加排序条件,按照菜品的创建时间排序
        queryWrapper.orderByDesc(Dishee::getCreateTime);
        //执行查询
        this.page(pageInfo, queryWrapper);

//        将封装好的pageInfo中的部分数据拷贝到新的dishDtoPage对象中
//        records属性中的数据不拷贝，records是一个集合属性，里面封装的就是查询到的菜品信息，需要进行处理后才能返回给前端
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //获取pageInfo对象中的records集合
        List<Dishee> records = pageInfo.getRecords();
        //使用stream流处理集合中的数据，处理后再收集成一个新的基于DishDto的集合
        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();//创建一个DishDto对象用于封装新的数据

            BeanUtils.copyProperties(item, dishDto);//将原始集合中的元素数据拷贝到dishDto对象中

            Long categoryId = item.getCategoryId();//获取菜品分类id
            Categoryee byId = category.getById(categoryId);//通过id查询菜品分类对象
            String name1 = byId.getName();//查询菜品分类对象中的name
            dishDto.setCategoryName(name1);//将分类名称赋值给DishDto对象中的categoryName属性
            return dishDto;
        }).collect(Collectors.toList());
        //将新的集合赋值给dishDtoPage对象中的records属性
        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);
    }

    @Override
    public R<DishDto> getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dishee byId = this.getById(id);
        //创建一个DishDto对象
        DishDto dishDto = new DishDto();
        //将查询到的菜品的基本信息拷贝到dishDto对象中
        BeanUtils.copyProperties(byId, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，按照菜品id进行查询
        queryWrapper.eq(DishFlavor::getDishId, byId.getId());
        //查询到的结果是一个菜品口味对象组成的集合
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return R.success(dishDto);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品表,dish
        this.updateById(dishDto);
        //清理dish_flavor表中当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();//条件构造器
        //添加查询条件，按照菜品id
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        //dish_flavor表中查询到的数据
        dishFlavorService.remove(queryWrapper);


        //添加当前提交过来的dishDto对象中封装的口味对象数据
        List<DishFlavor> flavors = dishDto.getFlavors();//获取新提交的口味数据
        //对集合中的数据进行处理，添加对应的菜品id,因为提交过来的flavors集合数据中没有菜品id
        List<DishFlavor> collect = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //将处理后的口味对象集合保存到dish_flavor表中
        dishFlavorService.saveBatch(collect);
    }

    @Override
    public void stopSales(Long ids) {
        dishMapper.stopSales(ids);
    }

    @Override
    public void startSales(Long ids) {
        dishMapper.startSales(ids);
    }


}
