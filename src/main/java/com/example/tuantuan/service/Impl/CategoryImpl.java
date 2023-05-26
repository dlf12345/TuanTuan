package com.example.tuantuan.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tuantuan.Mapper.CategoryMapper;
import com.example.tuantuan.domain.Categoryee;
import com.example.tuantuan.service.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Categoryee> implements Category {
    @Autowired
    private CategoryMapper categoryMapper;

    public int deleteById(Long ids){
        int i = categoryMapper.deleteById(ids);
        return i;
    }

}
