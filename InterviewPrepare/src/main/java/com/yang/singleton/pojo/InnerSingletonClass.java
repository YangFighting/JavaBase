package com.yang.singleton.pojo;

/**
 * 静态内部类的单例
 * 在内部类被加载和初始化时，才创建实例对象
 * 静态内部类不会自动随着外部类的加载和初始化而初始化，它是要单独去加载和初始化的。
 * 因为是在内部类加载和初始化时创建的，因此是线程安全的
 */
public class InnerSingletonClass {
    private String name;

    private InnerSingletonClass() {
    }

    private static class Inner {
        private static final InnerSingletonClass instance = new InnerSingletonClass();
    }


    public static InnerSingletonClass getInstance() {
        return Inner.instance;
    }
}
