package com;

/**
 * 默认类加载器测试、
 */
public class DefaultClassLoaderTest {

    public static void main(String[] args) {
        ClassLoader cl = DefaultClassLoaderTest.class.getClassLoader();
        System.out.println("ClassLoader is:" + cl.toString());
        System.out.println("ClassLoader\'s parent is:" + cl.getParent().toString());


        cl = int.class.getClassLoader();
        System.out.println("ClassLoader is:" + cl.toString());

    }
}
