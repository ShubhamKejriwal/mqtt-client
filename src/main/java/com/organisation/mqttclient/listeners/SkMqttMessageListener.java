/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.listeners;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.organisation.mqttclient.client.ISkMqttClient;
import com.organisation.mqttclient.client.SkPahoMqttClient;
import com.organisation.mqttclient.domains.SkMqttMessage;

/**
 * This is the callback handler (listener) class that would be listening to callback from ActiveMQ
 * indicating message arrival.
 * 
 * @author Shubham Kejriwal
 * 
 */
public class SkMqttMessageListener implements IMqttMessageListener {

    private SkPahoMqttClient observerClient;
    private SkMqttMessage receivedMessage;
    
    public SkMqttMessageListener() {
        
    }
    

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        
        this.observerClient.messageArrived(topic, message);
    }

    public ISkMqttClient getObserverClient() {
        return this.observerClient;
    }

    public void setObserverClient(final ISkMqttClient observerClient) {
        this.observerClient = (SkPahoMqttClient)observerClient;
    }
    
    public SkMqttMessage getReceivedMessage() {
        return this.receivedMessage;
    }

    public void setReceivedMessage(final SkMqttMessage receivedMessage) {
        this.receivedMessage = receivedMessage;
    }
}
