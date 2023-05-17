package com.example.tuantuan;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tuantuan.Mapper.EmployMapper;
import com.example.tuantuan.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TuanTuanApplicationTests {
    @Autowired
    private EmployMapper employMapper;
    @Test
    public void contextLoads() {
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        Integer integer = employMapper.selectCount(queryWrapper);

        System.out.println(integer);


    }

}
