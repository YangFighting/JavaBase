package com.yang.dao;

import com.yang.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2021/12/07 23:13
 **/
public interface UserDao {
    List<User> getUserList();

    List<User> getUserListByLimit(Map<String, Integer> map);

    @Select("select * from user;")
    List<User> selectUserList();

}
