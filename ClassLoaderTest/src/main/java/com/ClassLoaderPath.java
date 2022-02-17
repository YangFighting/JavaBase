package com;
/**
 * 类加载路径测试
 * */
public class ClassLoaderPath {

    public static void main(String[] args) {
        System.out.println("bootClassPath: " + System.getProperty("sun.boot.class.path"));
        System.out.println("ExtClassLoader Path: " + System.getProperty("java.ext.dirs"));
        System.out.println("AppClassLoader Path: " + System.getProperty("java.class.path"));
    }
}
