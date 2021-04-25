package com.yaohl0911.dao;


import com.yaohl0911.pojo.Student;

public interface StudentMapper {
    //根据id查询数据
    Student getStudentById(int id);

    Student getStudentById2(int id);
}
