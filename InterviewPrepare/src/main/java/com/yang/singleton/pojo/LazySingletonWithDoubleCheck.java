package com.yang.singleton.pojo;

public class LazySingletonWithDoubleCheck {
    private volatile static LazySingletonWithDoubleCheck instance = null;
    private String name;

    private LazySingletonWithDoubleCheck() {

    }

    public static LazySingletonWithDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (LazySingletonWithDoubleCheck.class) {
                if (instance == null) {
                    instance = new LazySingletonWithDoubleCheck();
                }
            }
        }
        return instance;
    }
}
