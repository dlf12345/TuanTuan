package com.example.tuantuan.service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Exception.CustomException;
import com.example.tuantuan.Mapper.CategoryMapper;
import com.example.tuantuan.domain.Categoryee;
import com.example.tuantuan.domain.Dishee;
import com.example.tuantuan.domain.Setmealee;
import com.example.tuantuan.service.Category;
import com.example.tuantuan.service.Dish;
import com.example.tuantuan.service.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Categoryee> implements Category {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private Dish dish;
    @Autowired
    private Setmeal setmeal;


    //根据id删除类别
    public int deleteById(Long ids){
        //首先需要判断该id下的类别是否关联了菜品和套餐，如果关联的话不可以删除
        QueryWrapper<Dishee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",ids);
        int count01 = dish.count(queryWrapper);
        if(count01>0){
            throw new CustomException("此类别关联了菜品，不可以删除");
            //该类别关联了菜品
        }
        QueryWrapper<Setmealee> queryWrapper1 =  new QueryWrapper<>();
        queryWrapper1.eq("category_id",ids);
        int count02 = setmeal.count(queryWrapper1);
        if(count02>0){
            throw new CustomException("此类别关联了套餐，不可以删除");
            //该类别关联了套餐
        }
        //正常删除
        int i = categoryMapper.deleteById(ids);
        return i;
    }

}
