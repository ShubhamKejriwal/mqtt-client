package com.organisation.mqttclient.activemq.test;

import com.organisation.mqttclient.factory.BeanFactory;

public abstract class AbstractBaseTest {

    public BeanFactory beanFactory;
    
    public AbstractBaseTest() {
        this.beanFactory = new BeanFactory();
    }
}
