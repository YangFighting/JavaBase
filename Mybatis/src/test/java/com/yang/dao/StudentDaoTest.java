package com.yang.dao;

import com.yang.pojo.Student;
import com.yang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/09 22:55
 **/
public class StudentDaoTest {
    private static final org.apache.log4j.Logger Logger = org.apache.log4j.Logger.getLogger(StudentDaoTest.class);

    @Test
    public void getStudentListTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        int studentId = 1;
        List<Student> studentList = mapper.getStudentList(studentId);
        for (Student student :
                studentList) {
            Logger.warn(student);
        }
        sqlSession.close();
    }

    @Test
    public void getStudentListCombinationQuery(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentList = mapper.getStudentListCombinationQuery();
        for (Student student :
                studentList) {
            Logger.warn(student);
        }
        sqlSession.close();
    }

    @Test
    public void getStudentListSubqueryTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> studentListSubquery = mapper.getStudentListSubquery();
        for (Student s :
                studentListSubquery) {
            Logger.warn(s);
        }
        sqlSession.close();
    }
}
