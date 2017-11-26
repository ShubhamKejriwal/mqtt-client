/*
 * Copyright 2017 Shubham Kejriwal.
 * All rights reserved.
 */

package com.organisation.mqttclient.constants;

/**
 * Class to store constants for MQTT constants.
 *
 * @author Shubham Kejriwal
 *
 */
public class SkMqttConstants {

    private SkMqttConstants() {

    }

    /**
     * Reliable Messaging:
     * MQTT allows a client to publish a message with the 
     * Quality of Service parameters (QoS).
     */
    public static final class QoS {

        /**
         * At most once delivery: The sender tries with best effort to send the
         * message and relies on the reliability of TCP. No retransmission takes
         * place.
         */
        public static final int AT_MOST_ONCE = 0;

        /**
         * At least once delivery: The receiver will get the message at least
         * once. If the receiver does not acknowledge the message or the
         * acknowledge gets lost on the way, it will be resent until the sender
         * gets an acknowledgement. Duplicate messages can occur.
         */       
        public static final int AT_LEAST_ONCE = 1;

        /**
         * Exactly once delivery: The protocol makes sure that the message will
         * arrive exactly once at the receiver. This increases communication
         * overhead but is the best option when neither loss nor duplication of
         * messages are acceptable.
         */
        public static final int EXACTLY_ONCE = 2;

        private QoS() {

        }
    }
}
