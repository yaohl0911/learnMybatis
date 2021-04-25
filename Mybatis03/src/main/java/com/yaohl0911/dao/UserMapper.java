package com.yaohl0911.dao;


import com.yaohl0911.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    // 获取全部限制范围内的全部数据
    List<User> getUserList(Map<String, Object> map);

    //根据id查询数据
    User getUserById(int id);

    // 插入一条数据
    int addUser(Map<String, Object> map);

    // 修改一条数据
    int updateUser(User user);

    // 删除一条数据
    int deleteUser(int id);

    List<User> getUserLike(String name);
}
