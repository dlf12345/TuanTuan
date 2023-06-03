package com.example.tuantuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
     //添加购物车
     R<ShoppingCart> add(ShoppingCart shoppingCart);
     //返回当前用户购物车信息
     R<List<ShoppingCart>> List();
     //移除购物车
     R<ShoppingCart> sub(ShoppingCart shoppingCart);
}
