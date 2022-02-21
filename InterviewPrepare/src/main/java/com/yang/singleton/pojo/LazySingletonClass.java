package com.yang.singleton.pojo;


/**
 * 懒汉式单例
 * 优点：延迟加载(Lazy Load)技术，在第一次调用getInstance()方法时实例化，在类加载时并不自行实例化
 * 缺点：多个线程同时调用getInstance()方法，存在线程不安全的问题
 *
 * */
public class LazySingletonClass {
    String name;
    private static LazySingletonClass instance = null;

    private LazySingletonClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LazySingletonClass getInstance() {
        if (instance == null) {
            instance = new LazySingletonClass();
        }
        return LazySingletonClass.instance;
    }
}
