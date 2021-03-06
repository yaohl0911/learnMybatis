package com.yaohl0911.dao;


import com.yaohl0911.pojo.User;

import java.util.List;

public interface UserMapper {
    // 获取全部数据
    List<User> getUserList();

    //根据id查询数据
    User getUserByID(int id);

    // 插入一条数据
    int addUser(User user);

    // 修改一条数据
    int updateUser(User user);

    // 删除一条数据
    int deleteUser(int id);

    List<User> getUserLike(String name);
}
