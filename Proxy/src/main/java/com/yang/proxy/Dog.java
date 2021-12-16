package com.yang.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  RealSubject
 *
 * @author zhangyang
 * @date 2021/12/16 17:39
 */

public class Dog implements Animal {
    private static final Logger logger = LoggerFactory.getLogger(Dog.class);


    @Override
    public void voice() {
        logger.warn("Dog: wang wang wang");
    }
}
