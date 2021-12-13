package com.yang.dao;

import com.yang.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/09 22:43
 **/
public interface StudentMapper {
    
    List<Student> getStudentList(@Param("id") int id);
    List<Student> getStudentListSubquery();
    List<Student> getStudentListCombinationQuery();
}
