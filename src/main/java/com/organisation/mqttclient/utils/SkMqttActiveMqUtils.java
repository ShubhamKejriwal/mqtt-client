/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.utils;

import org.springframework.util.StringUtils;

import com.organisation.mqttclient.exceptions.SkMqttException;

/**
 * Mqtt utility class for defining helper methods related to client operation
 * management.
 *
 * @author Shubham Kejriwal
 * 
 */
public class SkMqttActiveMqUtils {

    /**
     * Gets topic name with mqtt supported characters.
     * 
     * @param activeMQTopic Activemq supported topic name.
     * @return Topic name with mqtt supported characters. @see {@link http://activemq.apache.org/mqtt.html}.
     * @throws @see {@link SkMqttException}.
     */
    public static String getMqttTopic(final String activeMQTopic) throws SkMqttException {
        
        if (StringUtils.isEmpty(activeMQTopic)) {

            throw new SkMqttException("Topic can not be null or empty.");
        }
        String mappedTopic = activeMQTopic.replaceAll("\\.", "/").replaceAll("\\*", "+").replaceAll(">", "#");
        return mappedTopic;
    }
    
    /**
     * @see Gets topic names with mqtt supported characters.
     * 
     * @param activeMQTopic Activemq supported topic names.
     * @return Topic names with mqtt supported characters. @see {@link http://activemq.apache.org/mqtt.html}.
     * @throws @see {@link SkMqttException}.
     */
    public static String[] getMqttTopics(final String[] activeMQTopics) throws SkMqttException {
        
        String[] mqttTopics;
        if (activeMQTopics == null || activeMQTopics.length == 0) {

            throw new SkMqttException("Topics array can not be null or empty.");
        }

        mqttTopics = new String[activeMQTopics.length];
        for (int i = 0; i < activeMQTopics.length; i++) {
            mqttTopics[i] = getMqttTopic(activeMQTopics[i]);
        }
        return mqttTopics;
    }
}
