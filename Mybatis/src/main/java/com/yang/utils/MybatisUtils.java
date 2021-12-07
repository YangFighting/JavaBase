package com.yang.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author zhangyang
 * @date 2021/12/07 22:47
 **/
public class MybatisUtils {
    private static final SqlSessionFactory sqlSessionFactory;
    static {
        // 使用 mybatis 获取 SqlSessionFactory 实例
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSession getSqlSession() {
        // 从 SqlSessionFactory 中获取 SqlSession
        return sqlSessionFactory.openSession();
    }
}
