package com.yang.singleton;


import com.yang.singleton.pojo.*;

public class SingletonTest {


    public static void main(String[] args) {
        EagerSingletonClass eagerSingletonClass1 = EagerSingletonClass.getInstance();
        EagerSingletonClass eagerSingletonClass2 = EagerSingletonClass.getInstance();
        System.out.println(eagerSingletonClass1);
        System.out.println(eagerSingletonClass2);


        LazySingletonClass lazySingletonClass1 = LazySingletonClass.getInstance();
        LazySingletonClass lazySingletonClass2 = LazySingletonClass.getInstance();
        System.out.println(lazySingletonClass1);
        System.out.println(lazySingletonClass2);

        InnerSingletonClass innerSingletonClass1 = InnerSingletonClass.getInstance();
        InnerSingletonClass innerSingletonClass2 = InnerSingletonClass.getInstance();
        System.out.println(innerSingletonClass1);
        System.out.println(innerSingletonClass2);

        SingletonEnum singletonClass3 = SingletonEnum.INSTANCE;
        SingletonEnum singletonClass4 = SingletonEnum.INSTANCE;
        System.out.println(singletonClass3.getClass());
        System.out.println(singletonClass4.getClass());

        SingletonEnum instance1 = SingletonEnum.getInstance();
        SingletonEnum instance2 = SingletonEnum.getInstance();
        System.out.println(instance1.getClass());
        System.out.println(instance2.getClass());


        EnumSingletonClass enumSingletonClass1 = EnumSingletonClass.getInstnce();
        EnumSingletonClass enumSingletonClass2 = EnumSingletonClass.getInstnce();

        System.out.println(enumSingletonClass1.getClass());
        System.out.println(enumSingletonClass2.getClass());

    }
}
