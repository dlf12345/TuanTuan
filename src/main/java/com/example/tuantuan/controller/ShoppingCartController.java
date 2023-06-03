package com.example.tuantuan.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.ShoppingCart;
import com.example.tuantuan.service.ShoppingCartService;
import com.example.tuantuan.utils.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    //获取当前用户购物车里面的数据
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        return shoppingCartService.List();
    }


    //加入购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
       return shoppingCartService.add(shoppingCart);
    }
    //移出购物车
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.sub(shoppingCart);
    }

    //清空购物车
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
