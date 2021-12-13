package com.yang.pojo;

/**
 * @author zhangyang
 * @date 2021/12/09 22:37
 **/
public class Student {
    private int id;
    private String name;
    private Teacher teacher;

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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher=" + teacher.toString() +
                '}';
    }
}
