/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.exceptions;

/**
 * Custom exception wrapper.
 * 
 * @author Shubham Kejriwal
 *
 */
public class SkMqttException extends Exception {

    private static final long serialVersionUID = 1L;

    public SkMqttException(final String message) {
        super(message);
    }
}
