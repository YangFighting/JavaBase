package com.yang.pojo;

/**
 * @author zhangyang
 * @date 2021/12/09 22:37
 **/
public class Student1vN {
    private int id;
    private String name;
    private String tid;

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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "Student1vN{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tid='" + tid + '\'' +
                '}';
    }
}
