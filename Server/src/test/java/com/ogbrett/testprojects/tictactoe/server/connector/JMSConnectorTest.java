package com.ogbrett.testprojects.tictactoe.server.connector;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTConnectionRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.server.mocks.RequestProcessorMock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JMSConnectorTest {
    private static final String QUEUE_NAME = "TEST_QUEUE";
    @Test
    public void test() throws Exception {
        JMSConnector jmsConnector =  new JMSConnector("tcp://localhost:61613", QUEUE_NAME, new RequestProcessorMock());
        ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory("tcp://localhost:61613");
        connFactory.setTrustedPackages(Collections.singletonList("com.ogbrett.testprojects.tictactoe"));
        Connection connection = connFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination replyDest = session.createTemporaryQueue();
        Destination dest = session.createQueue(QUEUE_NAME);
        MessageConsumer replyConsumer = session.createConsumer(replyDest);
        MessageProducer producer = session.createProducer(dest);
        ObjectMessage message = session.createObjectMessage();
        message.setJMSReplyTo(replyDest);
        message.setJMSCorrelationID("test_id1");
        TTTRequest request = new TTTConnectionRequest("test_login");
        message.setObject(request);
        connection.start();
        assertNull(replyConsumer.receiveNoWait());

        producer.send(message);
        ObjectMessage replyMessage = (ObjectMessage)replyConsumer.receive(100);
        TTTResponse response = (TTTResponse)replyMessage.getObject();
        assertEquals(TTTResponse.Status.OK, response.getStatus());

        connection.stop();
        producer.close();
        replyConsumer.close();
        session.close();
        connection.close();
        jmsConnector.close();
    }
}
