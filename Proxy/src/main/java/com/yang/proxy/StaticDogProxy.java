package com.yang.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangyang
 * @date 2021/12/16 17:41
 */
public class StaticDogProxy implements Animal {
    private static final Logger logger = LoggerFactory.getLogger(StaticDogProxy.class);

    /** 可以改成 Dog dog */
    private final Animal dog;

    /** 入参也可以改成 Dog dog */
    public StaticDogProxy(Animal dog) {
        this.dog = dog;
    }

    @Override
    public void voice() {
        logger.warn("before Dog");
        dog.voice();
        logger.warn("after Dog");
    }
}
