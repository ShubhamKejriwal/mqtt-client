/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.domains;

import java.util.Date;

/**
 * Message.
 * 
 * @author Shubham Kejriwal
 * 
 */
public class SkMqttMessage {

    /**
     * Unique message identifier.
     */
    private int messageId;
    
    /**
     * Message.
     */
    private String message;
    
    /**
     * Topic on which message arrived.
     */
    private String topic;
    
    /**
     * Message timestamp.
     */
    private Date timestamp;
    
    /**
     * Quality of service associated with the message.
     */
    private int qos;
    
    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final int messageId) {
        this.messageId = messageId;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }
}
