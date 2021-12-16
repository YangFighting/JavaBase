package com.yang.proxy;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author zhangyang
 * @date 2021/12/16 18:30
 */
public class DynamicDogProxyTest {

    @Test
    public void newProxyInstance() {
        // 不能改成 Dog dog = (Dog) new DynamicDogProxy().newProxyInstance(new Dog());
        Animal dog = (Animal) new DynamicDogProxy().newProxyInstance(new Dog());
        Assert.assertNotNull(dog);
        dog.voice();
    }
}