package com.yang.dao;
import com.yang.pojo.User;
import com.yang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;
import sun.rmi.runtime.NewThreadAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2021/12/07 23:25
 **/
public class UserDaoTest {
    private static final Logger Logger = org.apache.log4j.Logger.getLogger(UserDaoTest.class);

    @Test
    public void getUserListTest() {

        // 获取 sqlSession 对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        // 执行 sql
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();
        for (User user :
                userList) {
            Logger.warn(String.valueOf(user));
        }
        // 关闭 sqlSession
        sqlSession.close();
    }

    @Test
    public void getUserListByLimitTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserDao mapper = sqlSession.getMapper(UserDao.class);
        Map<String ,Integer> mapLimit = new HashMap<>();
        mapLimit.put("startIndex", 1);
        mapLimit.put("pageSize", 1);
        List<User> userListByLimit = mapper.getUserListByLimit(mapLimit);
        for (User user :
                userListByLimit) {
            Logger.warn(user);
        }
    }
}
