/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.domains;

/**
 * Connection details for a client to publish/subscribe to 
 * messaging broker.
 * 
 * @author Shubham Kejriwal
 * 
 */
public class SkMqttConnection {

    /**
     * Messaging server URL.
     */
    private String brokerURL;
    
    /**
     * Port on which the messaging server is listening to client
     * connections.
     */
    private String port;
    
    /**
     * Username of the connecting client.
     */
    private String username;
    
    /**
     * Password for the connecting client.
     */
    private String password;
    
    /**
     * Indicates whether or not the client's session should be
     * persisted in the database. Defines durable/non-durable 
     * connection.
     */
    private boolean cleanSession;
    
    public String getBrokerURL() {
        return this.brokerURL;
    }
    
    public void setBrokerURL(final String brokerURL) {
        this.brokerURL = brokerURL;
    }
    
    public String getPort() {
        return this.port;
    }
    
    public void setPort(final String port) {
        this.port = port;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public boolean isCleanSession() {
        return this.cleanSession;
    }
    
    public void setCleanSession(final boolean cleanSession) {
        this.cleanSession = cleanSession;
    }
}
