package com.yang.dao;

import com.yang.pojo.Blog;
import com.yang.utils.IdUtils;
import com.yang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2021/12/12 22:12
 **/
public class BlogMapperTest {
    private static final org.apache.log4j.Logger Logger = org.apache.log4j.Logger.getLogger(BlogMapperTest.class);

    @Test
    public void getBlogListTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        List<Blog> blogList = blogMapper.getBlogList();

        for (Blog blog :
                blogList) {
            Logger.warn(blog);
        }

        sqlSession.close();
    }

    @Test
    public void addBlogTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = new Blog();
        blog.setId(String.valueOf(IdUtils.genItemId()));
        blog.setTitle("Mybatis 如此简单");
        blog.setAuthor("yang");
        blog.setCreateTime(new Date());
        blog.setViews(123);
        Assert.assertTrue(mapper.addBlog(blog) > 0);

        blog.setId(String.valueOf(IdUtils.genItemId()));
        blog.setTitle("Java 如此简单");
        blog.setCreateTime(new Date());
        Assert.assertTrue(mapper.addBlog(blog) > 0);

        blog.setId(String.valueOf(IdUtils.genItemId()));
        blog.setTitle("Spring 如此简单");
        blog.setCreateTime(new Date());
        Assert.assertTrue(mapper.addBlog(blog) > 0);

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void queryBlogTest() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("title", "%Java%");
        List<Blog> blogList = blogMapper.queryBlog(queryMap);

        for (Blog blog :
                blogList) {
            Logger.warn(blog);
        }

        sqlSession.close();
    }

}
