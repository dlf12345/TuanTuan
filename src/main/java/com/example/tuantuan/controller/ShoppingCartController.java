package com.example.tuantuan.controller;

import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.ShoppingCart;
import com.example.tuantuan.service.ShoppingCartService;
import com.example.tuantuan.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    //获取当前用户购物车里面的数据
    @GetMapping("/list")
    public R<ShoppingCart> list(){
        return null;
    }


    //加入购物车
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart){
        //为获取到的数据设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //判断当前用户在数据表中是否已经存在，如果已经存在只需要在原来的数量上加一即可，如果没有再添加
        //（1）判断添加的是菜品还是套餐
        if(shoppingCart.getDishId()!=null){

        }else{

        }


        return R.success("添加成功");
    }

}
