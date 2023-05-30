package com.example.tuantuan.dto;
import com.example.tuantuan.domain.DishFlavor;
import com.example.tuantuan.domain.Dishee;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
//相当于将菜品实体类和口味实体类结合
@Data
public class DishDto extends Dishee {
    //使用集合封装新添加的菜品口味
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
