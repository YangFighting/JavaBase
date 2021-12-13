package com.yang.pojo;

import java.util.List;

/**
 * @author zhangyang
 * @date 2021/12/09 22:35
 **/
public class Teacher1vN {
    private int id;
    private String name;
    private List<Student> students;

    public Teacher1vN() {
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher1vN{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
