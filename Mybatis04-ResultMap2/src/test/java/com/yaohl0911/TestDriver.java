package com.yaohl0911;

import com.yaohl0911.dao.TeacherMapper;
import com.yaohl0911.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class TestDriver {
    @Test
    public void getTeacherTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        System.out.println(mapper.getTeacherById(1));
        sqlSession.close();
    }
}
