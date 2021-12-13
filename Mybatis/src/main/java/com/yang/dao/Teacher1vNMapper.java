package com.yang.dao;


import com.yang.pojo.Teacher1vN;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/09 22:41
 **/
public interface Teacher1vNMapper {

    List<Teacher1vN> getTeacherList();
    List<Teacher1vN> getTeacherListSubquery();

}
