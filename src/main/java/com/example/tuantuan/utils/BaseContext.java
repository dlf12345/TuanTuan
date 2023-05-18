package com.example.tuantuan.utils;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * ThreadLocal类用来提供线程内部的局部变量，不同的线程之间不会相互干扰
 * 本项目中需要获取当前用户的id用于设置元数据处理器中的值，但是无法在元数据处理器中从session中获取数据
 * 于是选择在过滤器中先将用户id存储到ThreadLocal开辟的空间中，而元数据器的加载于过滤器的加载在同一个线程中，可以共享这个数据
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     * @param
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}