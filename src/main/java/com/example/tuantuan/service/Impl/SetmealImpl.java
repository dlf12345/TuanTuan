package com.example.tuantuan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.SetmealMapper;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.SetmealDish;
import com.example.tuantuan.domain.Setmealee;
import com.example.tuantuan.dto.DishDto;
import com.example.tuantuan.dto.SetmealDto;
import com.example.tuantuan.service.Category;
import com.example.tuantuan.service.Setmeal;
import com.example.tuantuan.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealImpl extends ServiceImpl<SetmealMapper, Setmealee> implements Setmeal {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private Category category;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addWithDish(SetmealDto setmealDto) {
        //添加基本套餐信息
        this.save(setmealDto);
        //添加套餐中的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //经过处理后的套餐菜品集合
        List<SetmealDish> collect = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //将套餐菜品信息保存到setmeal_dish表格中
        setmealDishService.saveBatch(collect);
    }

    @Override
    public R<Page> page(int page, int pageSize, String name) {
        //构造一个分页构造器
        Page<Setmealee> pageInfo = new Page<>();
        //构造一个基于SetmealDto的分页构造器
        Page<SetmealDto> dtoPageInfo = new Page<>();
        //构造一个条件构造器
        LambdaQueryWrapper<Setmealee> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.like(name != null, Setmealee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmealee::getCreateTime);
        //执行分页查询
        this.page(pageInfo, queryWrapper);

//        将pageInfo中的部分数据拷贝到dtoPageInfo中
//        records属性是一个集合，里面保存的就是查询到的套餐信息，需要经过处理后才能返回前端
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");

        List<Setmealee> records = pageInfo.getRecords();
        List<SetmealDto> collect = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
//            将集合中保存的套餐元素拷贝到新的dto对象中
            BeanUtils.copyProperties(item, setmealDto);
//            为SetmealDto对象设置categoryname
            setmealDto.setCategoryName(category.getById(item.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
//        将处理后的集合保存到dtoPageInfo对象中
        dtoPageInfo.setRecords(collect);
        return R.success(dtoPageInfo);
    }

    @Override
    public R<SetmealDto> getByIdWithDish(Long id) {
        //查询套餐基本信息
        Setmealee byId = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //将基本套餐信息拷贝到 SetmealDto对象中
        BeanUtils.copyProperties(byId,setmealDto);
        //通过套餐id在setmeal_dish表查询对应的套餐菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐表
        this.updateById(setmealDto);
        //删除setmeal_dish表中该套餐对应的套餐菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加新的套餐菜品信息到setmeal_dish表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//        由于传输过来的setmealdish数据中没有setmeal_id,所以需要先处理一下
        List<SetmealDish> collect = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //将处理后的集合数据添加到setmeal_dish表中
        setmealDishService.saveBatch(collect);
    }

    @Override
    public void stopSales(Long id) {
        setmealMapper.stopSales(id);
    }

    @Override
    public void startSales(Long id) {
        setmealMapper.startSales(id);
    }

    @Override
    public void deleteById(Long id) {
        //删除套餐表中的数据
        this.removeById(id);
        //删除套餐菜品表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(queryWrapper);
    }
}
