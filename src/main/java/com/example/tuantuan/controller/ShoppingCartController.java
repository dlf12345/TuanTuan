package com.example.tuantuan.controller;

import com.example.tuantuan.domain.R;
import com.example.tuantuan.domain.ShoppingCart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @GetMapping("/list")
    public R<ShoppingCart> list(){
        return null;
    }
}
