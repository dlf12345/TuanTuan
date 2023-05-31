package com.example.tuantuan.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tuantuan.domain.Setmealee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmealee> {
    @Update("update setmeal set status = 0 where id = #{id}")
    void stopSales(Long id);
    @Update("update setmeal set status = 1 where id = #{id}")
    void startSales(Long id);
}
