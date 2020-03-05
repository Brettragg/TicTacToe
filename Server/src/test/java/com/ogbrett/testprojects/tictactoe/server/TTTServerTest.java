package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.gamelogic.TTTException;
import com.ogbrett.testprojects.tictactoe.server.mock.MessageResponderStatusMock;
import com.ogbrett.testprojects.tictactoe.server.mock.MessageResponderTextMock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.*;

import javax.jms.*;

import static org.junit.Assert.*;

public class TTTServerTest {
    private static String TEST_URI = "tcp://localhost:61617";
    private static Queue TEST_QUEUE = new ActiveMQQueue("TestQueue");
    private static ConnectionFactory CONNECTION_FACTORY = new ActiveMQConnectionFactory(TEST_URI);

    private static TTTServer server;

    private Connection connection;
    private Session session;
    private Queue replyQueue;
    private MessageProducer messageProducer;
    private MessageConsumer messageReplyConsumer;

    @BeforeClass
    public static void globalSetUp() throws Exception {
        server = new TTTServer(TEST_URI);
    }

    @AfterClass
    public static void globalTearDown() throws Exception {
        server.close();
    }

    @Before
    public void localSetUp() throws Exception {
        server.start();
        connection = CONNECTION_FACTORY.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        replyQueue = session.createTemporaryQueue();
        messageProducer = session.createProducer(TEST_QUEUE);
        messageReplyConsumer = session.createConsumer(replyQueue);
        connection.start();
    }

    @After
    public void localTearDown() throws Exception {
        connection.stop();
        messageReplyConsumer.close();
        messageProducer.close();
        session.close();
        connection.close();
        if (server.isStarted()) {
            server.stop();
        }
        server.clearMessageResponders();
    }

    @Test
    public void testStart() {
        assertTrue(server.isStarted());
    }

    @Test
    public void testException() throws Exception {
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
        ActiveMQMessage message = new ActiveMQMessage();
        message.setJMSReplyTo(replyQueue);
        message.setCorrelationId("something");
        message.setStringProperty("Request", "Status");
        messageProducer.send(message);
        Message replyMessage = messageReplyConsumer.receive(1000);
        assertEquals("OK", replyMessage.getStringProperty("Status"));

    }

    @Test
    public void multipleResponders() throws Exception {
        server.addMessageResponder(new MessageResponderTextMock(), TEST_QUEUE);
        server.addMessageResponder(new MessageResponderStatusMock(), TEST_QUEUE);
        ActiveMQMessage message = new ActiveMQMessage();

        message.setJMSReplyTo(replyQueue);
        message.setCorrelationId("something");
        message.setStringProperty("Request", "Status");
        messageProducer.send(message);
        Message replyMessage = messageReplyConsumer.receive(1000);
        assertEquals("OK", replyMessage.getStringProperty("Status"));
        message.clearProperties();
        message.setJMSReplyTo(replyQueue);
        message.setCorrelationId("somethingelse");
        message.setStringProperty("Request", "Text");
        messageProducer.send(message);
        replyMessage = messageReplyConsumer.receive(1000);
        assertEquals("sample text", replyMessage.getStringProperty("Text"));
    }

    @Test
    public void startTwice() throws Exception {
        server.stop();
        server.addMessageResponder(new MessageResponderStatusMock(), TEST_QUEUE);
        server.start();
        ActiveMQMessage message = new ActiveMQMessage();
        message.setJMSReplyTo(replyQueue);
        message.setCorrelationId("something");
        message.setStringProperty("Request", "Status");
        messageProducer.send(message);
        Message replyMessage = messageReplyConsumer.receive(1000);
        assertEquals("OK", replyMessage.getStringProperty("Status"));
    }

    @Test
    public void testClear() throws Exception {
        server.addMessageResponder(new MessageResponderStatusMock(), TEST_QUEUE);
        server.clearMessageResponders();
        ActiveMQMessage message = new ActiveMQMessage();
        message.setJMSReplyTo(replyQueue);
        message.setCorrelationId("something");
        message.setStringProperty("Request", "Status");
        messageProducer.send(message);
        Message replyMessage = messageReplyConsumer.receiveNoWait();
        assertNull(replyMessage);
    }
}
