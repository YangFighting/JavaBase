package com.yang.dao;

import com.yang.pojo.User;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/07 23:13
 **/
public interface UserDao {
    List<User> getUserList();
}
