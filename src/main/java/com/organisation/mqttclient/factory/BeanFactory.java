/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.factory;

import com.organisation.mqttclient.client.ISkMqttClient;
import com.organisation.mqttclient.client.SkPahoMqttClient;
import com.organisation.mqttclient.domains.SkMqttConnection;

/**
 * Instantiates bean.
 * 
 * @author Shubham Kejriwal
 *
 */
public class BeanFactory {

    public ISkMqttClient getPahoMqttClient() {
        return new SkPahoMqttClient();
    }
    
    public SkMqttConnection getClientConnection() {
        return new SkMqttConnection();
    }
}
