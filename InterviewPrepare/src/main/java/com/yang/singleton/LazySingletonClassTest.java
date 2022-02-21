package com.yang.singleton;

import com.yang.singleton.pojo.LazySingletonClass;
import com.yang.singleton.pojo.LazySingletonWithDoubleCheck;

public class LazySingletonClassTest implements Runnable {

    @Override
    public void run() {
//        LazySingletonClass lazySingletonClass = LazySingletonClass.getInstance();
//        System.out.println(lazySingletonClass);

        LazySingletonWithDoubleCheck lazySingletonClass = LazySingletonWithDoubleCheck.getInstance();
        System.out.println(lazySingletonClass);
    }


    public static void main(String[] args) {
        LazySingletonClassTest lazySingletonClassTest = new LazySingletonClassTest();

        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
    }
}
