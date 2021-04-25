package com.yaohl0911.dao;


import com.yaohl0911.pojo.Teacher;
import org.apache.ibatis.annotations.Param;

public interface TeacherMapper {
    //根据id查询数据
    Teacher getTeacherById(@Param("id") int id);
}
