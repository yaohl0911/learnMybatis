package com.yaohl0911;

import com.yaohl0911.dao.UserMapper;
import com.yaohl0911.pojo.User;
import com.yaohl0911.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDriver {
    @Test
    public void getUserListTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startIndex", 0);
        map.put("pageSize", 2);
        List<User> userList = userMapper.getUserList(map);
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void getUserByIDTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserById(1);
        System.out.println("Add user: " + user.toString());
        sqlSession.close();
    }

    @Test
    public void addUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", 4);
        map.put("NAME", "dd2");
        map.put("AGE", 2);
        int res = userMapper.addUser(map);
        if(res != 0) {
            System.out.println("Add user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int res = userMapper.updateUser(new User(4, "dd2", 1));
        if(res != 0) {
            System.out.println("Update user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void getUserLikeTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList= userMapper.getUserLike("%d%"); // sql代码执行的时候，传递通配符"% %"
        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void deleteUserTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int res = userMapper.deleteUser(4);
        if(res != 0) {
            System.out.println("Delete user succeed.");
        }
        sqlSession.commit();
        sqlSession.close();
    }
}
