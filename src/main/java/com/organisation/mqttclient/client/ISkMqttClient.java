/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.client;

import java.util.List;

import com.organisation.mqttclient.domains.SkMqttConnection;
import com.organisation.mqttclient.domains.SkMqttMessage;
import com.organisation.mqttclient.exceptions.SkMqttException;

/**
 * Custom MQTT client interface.
 * 
 * @author Shubham Kejriwal
 */
public interface ISkMqttClient {

    /**
     * Set the connection details for the client.
     * 
     * @param mqttConnection
     * @throws @{@link SkMqttException} 
     */
    public void setMqttConnectionDetails(final SkMqttConnection mqttConnection) throws SkMqttException;
    
    /**
     * Get the connection details for the client.
     *
     * @return Connection details for the client
     */
    public SkMqttConnection getMqttConnectionDetails();

    /**
     * Connect to broker.
     * 
     * @throws @{@link SkMqttException}
     */
    public void connect() throws SkMqttException;
    
    /**
     * Method to check whether the client is connected to the broker.
     * 
     * @return Connection state of the client
     * @throws  @throws @{@link SkMqttException}
     */
    public boolean isConnected() throws SkMqttException;
    
    /**
     * Method to get the client id of client.
     * 
     * @return Client Id of the client
     * @throws @{@link SkMqttException}
     */
    public String getClientId() throws SkMqttException;
    
    /**
     * Method to publish a message to the broker.
     * 
     * @param topic Destination topic for the message
     * @param message Message to be published
     * @param retained Whether to retain the last message on the broker
     * @throws @{@link SkMqttException}
     */
    public void publish(final String topic, 
                        final String message,
                        final boolean retained) throws SkMqttException;
    
    /**
     * Method to subscribe to topic.
     * 
     * @param topics Destination topic to be subscribed to
     * @throws @{@link SkMqttException}
     */
    public void subscribe(final String topic) throws SkMqttException;
    
    /**
     * Method to subscribe to topic(s).
     * 
     * @param topics Destination topics to be subscribed to
     * @throws @{@link SkMqttException}
     */
    public void subscribe(final String[] topics) throws SkMqttException;
    
    /**
     * Method to get the list of messages received by a subscriber.
     * 
     * @return List of messages received
     * @throws @{@link SkMqttException}
     */
    public List<SkMqttMessage> getRecievedMessage() throws SkMqttException;
    
    /**
     * Method to get the list of messages published by the publisher.
     * 
     * @return List of messages published
     * @throws @{@link SkMqttException}
     */
    public List<SkMqttMessage> getPublishedMessage() throws SkMqttException;
    
    /**
     * Method to unsubscribe from topic.
     * 
     * @param topic Topic to unsubscribe from
     * @throws @{@link SkMqttException}
     */
    public void unsubscribe(final String topic) throws SkMqttException;
    
    /**
     * Method to unsubscribe from topic(s).
     * 
     * @param topics Topics to unsubscribe from
     * @throws @{@link SkMqttException}
     */
    public void unsubscribe(final String[] topics) throws SkMqttException;
    
    /**
     * Method to disconnect the client from the broker.
     * 
     * @throws @{@link SkMqttException}
     */
    public void disconnect() throws SkMqttException;
    
    /**
     * Releases all resource associated with the mqtt client. After the client
     * has been closed it cannot be reused
     * 
     * @throws SkMqttException
     */
    public void close() throws SkMqttException ;
}
