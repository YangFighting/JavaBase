package com.yang.singleton.pojo;

public class EnumSingletonClass {
    String name;

    private EnumSingletonClass() {
    }

    //定义一个静态的枚举类型
    enum SingletonClassEnum {
        //定义了枚举类型的实例对象
        INSTANCE;

        private EnumSingletonClass enumSingletonClass = null;

        //私有化枚举的构造函数
        SingletonClassEnum() {
            enumSingletonClass = new EnumSingletonClass();
        }

        public EnumSingletonClass getInstnce() {
            return enumSingletonClass;
        }
    }

    public static EnumSingletonClass getInstnce() {
        return SingletonClassEnum.INSTANCE.getInstnce();
    }

}
