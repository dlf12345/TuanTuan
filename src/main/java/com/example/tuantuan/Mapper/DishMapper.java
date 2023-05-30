package com.example.tuantuan.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tuantuan.domain.Dishee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dishee> {
    //停售
    @Update("update dish set status = 0 where id = #{ids}")
    void stopSales(Long ids);

    //起售
    @Update("update dish set status = 1 where id = #{ids}")
    void startSales(Long ids);
}
