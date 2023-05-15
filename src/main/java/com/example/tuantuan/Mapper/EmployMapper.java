package com.example.tuantuan.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tuantuan.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface EmployMapper extends BaseMapper<Employee> {
//    @Select("select * from employee where username = #{username}")
//    public Employee login(String username);
//    @Insert("insert into employee (name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user) values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},now(),now(),#{createUser},#{updateUser})")
//    public void save(Employee employee);
}
