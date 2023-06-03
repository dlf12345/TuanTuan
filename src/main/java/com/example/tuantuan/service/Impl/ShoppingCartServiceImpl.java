package com.example.tuantuan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.ShoppingCartMapper;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.ShoppingCart;
import com.example.tuantuan.service.ShoppingCartService;
import com.example.tuantuan.utils.BaseContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        //为获取到的数据设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //判断当前添加的数据在当前用户的购物车中是否已经存在，如果已经存在只需要在原来的数量上加一即可，如果没有再添加
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //添加当前用户条件
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        //（1）判断添加的是菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //(2)开始查询
        ShoppingCart one = this.getOne(queryWrapper);
        if (one != null) {
            //如果已经存在只需将number+1
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            this.updateById(one);
        } else {
            //如果不存在，将获取到的数据添加到数据库中即可
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            one = shoppingCart;
        }


        return R.success(one);
    }

    @Override
    public R<List<ShoppingCart>> List() {
        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();
        //查询当前用户的购物车信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> list = this.list(queryWrapper);
        return R.success(list);
    }

    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        //获取当前数据的id
        Long id = shoppingCart.getId();
       //查询该条数据的number值。如果大于1，直接减去1即可，如果等于1，需要将数据库表里的数据删除
        ShoppingCart byId = this.getById(id);
        Integer number = byId.getNumber();
        if(number>1){
            byId.setNumber(number-1);
            this.updateById(byId);
            return R.success(byId);
        }else{
            byId.setNumber(0);
            this.removeById(id);
            return R.success(byId);
        }
    }
}
