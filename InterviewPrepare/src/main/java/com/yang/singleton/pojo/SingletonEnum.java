package com.yang.singleton.pojo;

public enum SingletonEnum {
    INSTANCE;

    public static SingletonEnum getInstance() {
        return INSTANCE;
    }

    public void businessMethod() {
        // 处理业务方法
        System.out.println("我是一个单例！");
    }
}
