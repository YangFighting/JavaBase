package com.yang.proxy;

import org.junit.Assert;

import static org.junit.Assert.*;

/**
 * @author zhangyang
 * @date 2021/12/16 17:46
 */
public class StaticDogProxyTest {

    @org.junit.Test
    public void voiceTest() {
        Animal dog = new StaticDogProxy(new Dog());
        Assert.assertNotNull(dog);
        dog.voice();
    }
}