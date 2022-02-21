package com.yang.singleton.pojo;

/**
 * 饿汉式单例类
 * 优点：无须考虑多线程访问问题，可以确保实例的唯一性；从调用速度和反应时间角度来讲，由于单例对象一开始就得以创建，因此要优于懒汉式单例。
 * 缺点：由于在类加载时该对象就需要创建，因此从资源利用效率角度来讲，饿汉式单例不及懒汉式单例，而且在系统加载时由于需要创建饿汉式单例对象，加载时间可能会比较长
 */
public class EagerSingletonClass {

    String name;
    private static final EagerSingletonClass EAGER_SINGLETON_CLASS_INSTANCE = new EagerSingletonClass();

    public static EagerSingletonClass getInstance() {
        return EAGER_SINGLETON_CLASS_INSTANCE;
    }

    private EagerSingletonClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
