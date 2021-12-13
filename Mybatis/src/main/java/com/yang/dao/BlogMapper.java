package com.yang.dao;


import com.yang.pojo.Blog;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2021/12/12 22:09
 **/
public interface BlogMapper {
    List<Blog> getBlogList();

    int addBlog(Blog blog);

    List<Blog> queryBlog(Map<String,String> map);
}
