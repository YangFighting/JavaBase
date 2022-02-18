package com;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
//        DiskClassLoader diskLoader1 = new DiskClassLoader("D:\\lib\\test");
        DiskClassLoader diskLoader1 = new DiskClassLoader("D:\\lib");
        Class aClass = diskLoader1.loadClass("com.Test");
        System.out.println(aClass.getClassLoader().toString());
        if (aClass != null) {
            // 通过反射调用 Test类的say 方法
            Object obj = aClass.newInstance();
            Method say = aClass.getDeclaredMethod("say",  null);
            say.invoke(obj, null);
        }

        DiskClassLoader diskLoader = new DiskClassLoader("D:\\lib");
        System.out.println("Thread "+Thread.currentThread().getName()+" classloader: "
                +Thread.currentThread().getContextClassLoader().toString());
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 设置子线程的ContextClassLoader
                Thread.currentThread().setContextClassLoader(diskLoader1);

                System.out.println("Thread "+Thread.currentThread().getName()+" classloader: "+
                        Thread.currentThread().getContextClassLoader().toString());
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                try {
                    Class<?> c = cl.loadClass("com.Test");
                    System.out.println(c.getClassLoader().toString());
                    if (c != null) {
                        // 通过反射调用 Test类的say 方法
                        Object obj = c.newInstance();
                        Method say = c.getDeclaredMethod("say",  null);
                        say.invoke(obj, null);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
