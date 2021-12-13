package com.yang.dao;

import com.yang.pojo.Teacher1vN;
import com.yang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/09 23:08
 **/
public class TeacherDaoTest {

    private static final Logger Logger = org.apache.log4j.Logger.getLogger(StudentDaoTest.class);

    @Test
    public void getTeacherListTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        Teacher1vNMapper mapper = sqlSession.getMapper(Teacher1vNMapper.class);
        List<Teacher1vN> teacherList = mapper.getTeacherList();
        for (Teacher1vN teacher :
                teacherList) {
            Logger.warn(teacher);
        }
        sqlSession.close();
    }

    @Test
    public void getTeacherListSubqueryTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        Teacher1vNMapper mapper = sqlSession.getMapper(Teacher1vNMapper.class);
        List<Teacher1vN> teacherList = mapper.getTeacherListSubquery();
        for (Teacher1vN teacher :
                teacherList) {
            Logger.warn(teacher);
        }
        sqlSession.close();
    }
}
