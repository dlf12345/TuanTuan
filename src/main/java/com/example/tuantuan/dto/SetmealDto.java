package com.example.tuantuan.dto;

import com.example.tuantuan.domain.SetmealDish;
import com.example.tuantuan.domain.Setmealee;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmealee {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
