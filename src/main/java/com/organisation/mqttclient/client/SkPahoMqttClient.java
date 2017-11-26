/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.organisation.mqttclient.constants.SkMqttConstants;
import com.organisation.mqttclient.domains.SkMqttConnection;
import com.organisation.mqttclient.domains.SkMqttMessage;
import com.organisation.mqttclient.exceptions.SkMqttException;
import com.organisation.mqttclient.listeners.SkMqttMessageListener;
import com.organisation.mqttclient.utils.SkMqttActiveMqUtils;

/**
 * Paho MQTT Client implementation.
 * 
 * @author Shubham Kejriwal
 *
 */
public class SkPahoMqttClient implements ISkMqttClient {

    private static final Logger LOG = LoggerFactory.getLogger(SkPahoMqttClient.class);
    private static final int KEEP_ALIVE_INTERVAL = 180;

    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;    
    private SkMqttConnection mqttConnection;
    private List<SkMqttMessage> recievedMessageList = new ArrayList<SkMqttMessage>();
    private List<SkMqttMessage> publishedMessageList = new ArrayList<SkMqttMessage>();

    /**
     * Set the connection details for the client.
     * 
     * @param mqttConnection
     * @throws @{@link SkMqttException} 
     */
    public void setMqttConnectionDetails(SkMqttConnection mqttConnection) throws SkMqttException {

        try{

            if (mqttConnection == null) { 
                throw new SkMqttException("Connection details are not set for the client");
            }

            this.mqttConnection = mqttConnection;
            final String brokerURL = mqttConnection.getBrokerURL() + ":" + mqttConnection.getPort();  
            this.mqttClient = new MqttClient(brokerURL, MqttClient.generateClientId(), new MemoryPersistence());
            this.setMqttConnectOptions();

        } catch(MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Get the connection details for the client.
     *
     * @return Connection details for the client
     */
    public SkMqttConnection getMqttConnectionDetails() {
        return this.mqttConnection;
    }

    /**
     * Method to prepare the connection options for the client.
     * 
     * @return Connection options
     */
    private MqttConnectOptions setMqttConnectOptions() {

        this.mqttConnectOptions = new MqttConnectOptions();
        this.mqttConnectOptions.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
        this.mqttConnectOptions.setCleanSession(this.mqttConnection.isCleanSession());
        this.mqttConnectOptions.setUserName(this.mqttConnection.getUsername());
        this.mqttConnectOptions.setPassword(this.mqttConnection.getPassword().toCharArray());
        return this.mqttConnectOptions;
    }

    /**
     * Get the connection options for the client.
     * 
     * @return Client connection options
     */
    private MqttConnectOptions getMqttConnectOptions() {
        return this.mqttConnectOptions;
    }

    /**
     * Method that establishes connection to the broker.
     * 
     * @throws @{@link SkMqttException}
     */
    public void connect() throws SkMqttException {

        try{

            if (this.getMqttConnectOptions() == null) {               
                throw new SkMqttException("Connection details are not set for the client");
            }

            this.mqttClient.connect(this.getMqttConnectOptions());
            LOG.info("Client " + this.mqttClient.getClientId() + " connected to the broker successfully");

        } catch(MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to check whether the client is connected to the broker.
     * 
     * @return Connection state of the client
     */
    public boolean isConnected() throws SkMqttException {

        if (null == this.mqttClient) {
            return false;
        }
        return this.mqttClient.isConnected();
    }

    /**
     * Method to get the client id of client.
     * 
     * @return Client Id of the client
     */
    public String getClientId() throws SkMqttException {

        if (null == this.mqttClient) {
            return "";
        }
        return this.mqttClient.getClientId();
    }

    /**
     * Method to publish a message to the broker.
     * 
     * @param topic Destination topic for the message
     * @param message Message to be published
     * @param retained Whether to retain the last message on the broker
     * @throws @{@link SkMqttException}
     */
    public void publish(String topic, String message, boolean retained) throws SkMqttException {

        try {

            if (!this.isConnected()) {

                throw new SkMqttException("Client is not connected to broker.");
            } else if (message == null) {

                throw new SkMqttException("Message to be published can not be null.");              
            } else {

                String mqttTopic = SkMqttActiveMqUtils.getMqttTopic(topic);
                this.mqttClient.publish(mqttTopic, message.getBytes(), SkMqttConstants.QoS.EXACTLY_ONCE, retained);
                LOG.info("Client published message on the broker successfully.");
            }

        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to subscribe to topic.
     * 
     * @param topicFilter Destination topic to be subscribed to
     * @throws @{@link SkMqttException} 
     */
    public void subscribe(String topicFilter) throws SkMqttException {

        try {

            if (!this.isConnected()) {

                throw new SkMqttException("Client is not connected to broker.");
            } else {

                final String mqttTopic = SkMqttActiveMqUtils.getMqttTopic(topicFilter);

                //Registering callback handler for subscription request
                final SkMqttMessageListener mqttMessageListener = new SkMqttMessageListener();
                mqttMessageListener.setObserverClient(this);

                this.mqttClient.subscribe(mqttTopic, SkMqttConstants.QoS.EXACTLY_ONCE, mqttMessageListener);
                LOG.info("Client subscribed to topic successfully.");
            }

        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to subscribe to topic(s).
     * 
     * @param topicFilters Destination topics to be subscribed to
     * @throws @{@link SkMqttException}
     */
    public void subscribe(String[] topicFilters) throws SkMqttException {

        try {

            if (!this.isConnected()) {

                throw new SkMqttException("Client is not connected to broker.");
            } else {

                String[] mqttTopicFilters = SkMqttActiveMqUtils.getMqttTopics(topicFilters);
                int[] qosArray = new int[mqttTopicFilters.length];
                Arrays.fill(qosArray, SkMqttConstants.QoS.EXACTLY_ONCE);
                
                //Registering callback handler for subscription request
                final SkMqttMessageListener mqttMessageListener = new SkMqttMessageListener();
                mqttMessageListener.setObserverClient(this);
                final IMqttMessageListener[] listenerArray = new SkMqttMessageListener[mqttTopicFilters.length];
                Arrays.fill(listenerArray, mqttMessageListener);
                
                this.mqttClient.subscribe(mqttTopicFilters, qosArray, listenerArray);
                LOG.info("Client subscribed to topic(s) successfully.");
            }

        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to get the list of messages received by a subscriber. Returned
     * list will be empty if no message is received.
     * 
     * @return List of messages received
     * @throws @{@link SkMqttException}
     */
    public List<SkMqttMessage> getRecievedMessage() throws SkMqttException {

        return this.recievedMessageList;
    }

    /**
     * Method to get the list of messages published by the publisher. Returned
     * list will be empty if no message is published.
     * 
     * @return List of messages published
     * @throws @{@link SkMqttException}
     */
    public List<SkMqttMessage> getPublishedMessage() throws SkMqttException {

        return this.publishedMessageList;
    }

    /**
     * Method to unsubscribe from topic.
     * 
     * @param topicFilter Topic to unsubscribe from
     * @throws @{@link SkMqttException}
     */
    public void unsubscribe(String topicFilter) throws SkMqttException {

        try {

            if (!this.isConnected()) {

                throw new SkMqttException("Client is not connected to broker.");
            } else {

                LOG.info("Unsubscribing client from the topic...");
                String mqttTopic = SkMqttActiveMqUtils.getMqttTopic(topicFilter);
                this.mqttClient.unsubscribe(mqttTopic);
                LOG.info("Client unsubscribed from the topic successfully.");
            }
        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to unsubscribe from topic(s).
     * 
     * @param topicFilters Topics to unsubscribe from
     * @throws @{@link SkMqttException}
     */
    public void unsubscribe(String[] topicFilters) throws SkMqttException {

        try {

            if (!this.isConnected()) {

                throw new SkMqttException("Client is not connected to broker.");
            } else {

                LOG.info("Unsubscribing client from the topic(s)...");
                String[] mqttTopicFilters = SkMqttActiveMqUtils.getMqttTopics(topicFilters);
                this.mqttClient.unsubscribe(mqttTopicFilters);
                LOG.info("Client unsubscribed from the topic(s) successfully.");
            }
        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Method to disconnect the client from the broker.
     * 
     * @throws @{@link SkMqttException}
     */
    public void disconnect() throws SkMqttException {

        try{

            LOG.info("Disconnecting the client from the broker...");
            this.mqttClient.disconnect();
            LOG.info("Client is disconnected from the broker.");
        } catch(MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    /**
     * Releases all resource associated with the MQTT client. After the client
     * has been closed it cannot be reused
     * 
     * @throws @{@link SkMqttException}
     */
    public void close() throws SkMqttException {

        try {

            LOG.info("Closing the client...");
            this.mqttClient.disconnect();
            this.mqttClient.close();
            LOG.info("Client is closed now.");
        } catch (MqttException mqttException) {

            throw new SkMqttException(mqttException.getMessage());
        }
    }

    // Callback methods

    /**
     * This method is called when a message arrives from the server.
     *
     * @param topic Name of the topic on the message was published to
     * @param message Actual message.
     * @throws Exception if a terminal error has occurred, and the client should be
     * shut down.
     */
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        
        this.mqttClient.messageArrivedComplete(message.getId(), message.getQos());
        final SkMqttMessage receivedMessage = new SkMqttMessage();
        receivedMessage.setTopic(topic);
        receivedMessage.setMessageId(message.getId());
        receivedMessage.setMessage(message.toString());
        receivedMessage.setTimestamp(new Date());
        receivedMessage.setQos(message.getQos());
        this.recievedMessageList.add(receivedMessage);
    }
}
