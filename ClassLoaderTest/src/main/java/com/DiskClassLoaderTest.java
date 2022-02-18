package com;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiskClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        DiskClassLoader diskClassLoader = new DiskClassLoader("D:\\lib");
        Class<?> aClass = diskClassLoader.loadClass("com.Test");
        if (aClass != null) {
            // 通过反射调用 Test类的say 方法
            Object obj = aClass.newInstance();
            Method say = aClass.getDeclaredMethod("say",  null);
            say.invoke(obj, null);
        }
    }
}
