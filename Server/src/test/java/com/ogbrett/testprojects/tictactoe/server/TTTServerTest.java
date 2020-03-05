package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.gamelogic.TTTException;
import com.ogbrett.testprojects.tictactoe.server.mock.MessageResponderStatusMock;
import com.ogbrett.testprojects.tictactoe.server.mock.MessageResponderTextMock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.*;

public class TTTServerTest {
    private TTTServer server;
    private static String TEST_URI = "tcp://localhost:61617";
    private static Queue TEST_QUEUE = new ActiveMQQueue("TestQueue");

    @Before
    public void setUp() throws Exception {
        server = new TTTServer(TEST_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testStart() throws Exception {
        assertFalse(server.isStarted());
        try {
            server.start();
            assertTrue(server.isStarted());
        } finally {
            server.stop();
        }
        assertFalse(server.isStarted());
    }

    @Test
    public void testException() throws Exception {
        server.start();
        try {
            server.start();
            fail();
        } catch (TTTException e) {
            assertEquals(TTTException.SERVER_ALREADY_STARTED, e.getMessage());
        } finally {
            server.stop();
        }
        try {
            server.stop();
            fail();
        } catch (TTTException e) {
            assertEquals(TTTException.SERVER_ALREADY_STOPPED, e.getMessage());
        }
    }

    @Test
    public void receiveReply() throws Exception {
        server.addMessageResponder(new MessageResponderStatusMock(), TEST_QUEUE);
        server.start();
        ConnectionFactory connFactory = new ActiveMQConnectionFactory(TEST_URI);
        Connection connection = connFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue replyQueue = session.createTemporaryQueue();
        MessageProducer messageProducer = session.createProducer(TEST_QUEUE);
        MessageConsumer messageReplyConsumer = session.createConsumer(replyQueue);
        connection.start();
        try {
            ActiveMQMessage message = new ActiveMQMessage();
            message.setJMSReplyTo(replyQueue);
            message.setCorrelationId("something");
            message.setStringProperty("Request", "Status");
            messageProducer.send(message);
            Message replyMessage = messageReplyConsumer.receive(1000);
            assertEquals("OK", replyMessage.getStringProperty("Status"));
        } finally {
            connection.stop();
            messageReplyConsumer.close();
            messageProducer.close();
            session.close();
            connection.close();
            server.stop();
        }
    }

    @Test
    public void multipleResponders() throws Exception {
        server.addMessageResponder(new MessageResponderTextMock(), TEST_QUEUE);
        server.addMessageResponder(new MessageResponderStatusMock(), TEST_QUEUE);
        server.start();
        ConnectionFactory connFactory = new ActiveMQConnectionFactory(TEST_URI);
        Connection connection = connFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue replyQueue = session.createTemporaryQueue();
        MessageProducer messageProducer = session.createProducer(TEST_QUEUE);
        MessageConsumer messageReplyConsumer = session.createConsumer(replyQueue);
        connection.start();
        try {
            ActiveMQMessage message = new ActiveMQMessage();
            message.setJMSReplyTo(replyQueue);
            message.setCorrelationId("something");
            message.setStringProperty("Request", "Status");
            messageProducer.send(message);
            message.clearProperties();
            message.setJMSReplyTo(replyQueue);
            message.setCorrelationId("somethingelse");
            message.setStringProperty("Request", "Text");
            messageProducer.send(message);
            Message replyMessage = messageReplyConsumer.receive();
            assertEquals("OK", replyMessage.getStringProperty("Status"));
            replyMessage = messageReplyConsumer.receive(1000);
            assertEquals("sample text", replyMessage.getStringProperty("Text"));
        } finally {
            connection.stop();
            messageReplyConsumer.close();
            messageProducer.close();
            session.close();
            connection.close();
            server.stop();
        }
    }
}
