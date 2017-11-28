package com.organisation.mqttclient.activemq.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.organisation.mqttclient.client.ISkMqttClient;
import com.organisation.mqttclient.domains.SkMqttConnection;
import com.organisation.mqttclient.exceptions.SkMqttException;

public class DemoTest extends AbstractBaseTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(DemoTest.class);

    public ISkMqttClient publisher;
    private ISkMqttClient subscriber;
    
    private SkMqttConnection publisherMqttConnection;
    private SkMqttConnection subscriberMqttConnection;
    
    @BeforeClass(alwaysRun = true)
    public void setUp() throws SkMqttException {
        
        this.publisher = this.beanFactory.getPahoMqttClient();
        this.subscriber = this.beanFactory.getPahoMqttClient();
        
        //Set publisher connection details
        this.publisherMqttConnection = this.beanFactory.getClientConnection();
        this.publisherMqttConnection.setBrokerURL("ssl://<MESSAGING_SERVER_HOST>");
        this.publisherMqttConnection.setPort("<MESSAGING_SERVER_LISTENING_PORT>");
        this.publisherMqttConnection.setUsername("producer");
        this.publisherMqttConnection.setPassword("12345");
        this.publisherMqttConnection.setCleanSession(false);
        this.publisher.setMqttConnectionDetails(publisherMqttConnection);
        
        //Set subscriber connection details
        this.subscriberMqttConnection = this.beanFactory.getClientConnection();
        this.subscriberMqttConnection.setBrokerURL("ssl://<MESSAGING_SERVER_HOST>"); 
        this.subscriberMqttConnection.setPort("<MESSAGING_SERVER_LISTENING_PORT>");
        this.subscriberMqttConnection.setUsername("consumer");
        this.subscriberMqttConnection.setPassword("54321");
        this.subscriberMqttConnection.setCleanSession(false);
        this.subscriber.setMqttConnectionDetails(subscriberMqttConnection);
        
        LOG.info("Connect the publisher to the broker");
        this.publisher.connect();
        Assert.assertTrue(this.publisher.isConnected(), "Publisher failed to connect to broker");
        
        LOG.info("Connect the subscriber to the broker");
        this.subscriber.connect();
        Assert.assertTrue(this.subscriber.isConnected(), "Subscriber failed to connect to broker");
    }
    
    @BeforeMethod(alwaysRun = true)
    public void checkActiveClient() throws SkMqttException {
        
        if (null == this.publisher || null == this.subscriber) {
            Assert.fail("Publisher or Subscriber instance is not initialized");
        }
        
        Assert.assertTrue(this.publisher.isConnected(), "Publisher is not connected to broker");
        Assert.assertTrue(this.subscriber.isConnected(), "Subscriber is not connected to broker");
    }
    
    @Test
    public void test() throws SkMqttException, InterruptedException {
        
        LOG.info("Subscribe the subscriber to topic");
        this.subscriber.subscribe("testTopic");
        
        LOG.info("Publisher publishes message to the topic");
        publisher.publish("testTopic", "This is a test message", false);
        
        LOG.info("Wait for message delivery");
        Thread.sleep(5000);
        
        LOG.info("Verify that message is delivered to active subscriber");
        Assert.assertEquals(this.subscriber.getRecievedMessage().size(), 1, "Message not delivered to subscriber");
    }
    
    @AfterMethod(alwaysRun = true)
    public void refreshBrokerAndClient() throws SkMqttException {
        
        if (this.subscriber != null && this.subscriber.isConnected()) {
            
            LOG.info("Clear subscriber's message list and subscription details");
            this.subscriber.getRecievedMessage().clear();
            this.subscriber.unsubscribe("#");
        }
    }
    
    @AfterClass(alwaysRun = true)
    public void disconnectClientAndReleaseConnectionResources() throws SkMqttException {
        
        if (this.subscriber.isConnected()) {
            
            LOG.info("Disconnect the subscriber from broker and release resources");
            this.subscriber.unsubscribe("#");
            this.subscriber.close();
        }
        
        if (this.publisher.isConnected()) {
            
            LOG.info("Disconnect the publisher from broker and release resources");
            this.publisher.close();
        }
    }
}
