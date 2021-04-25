package com.yaohl0911;

import com.yaohl0911.dao.StudentMapper;
import com.yaohl0911.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class TestDriver {
    @Test
    public void getStudentTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        System.out.println(mapper.getStudentById(1));
        sqlSession.close();
    }

    @Test
    public void getStudentTest2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        System.out.println(mapper.getStudentById2(1));
        sqlSession.close();
    }
}
