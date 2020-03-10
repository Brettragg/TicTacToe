package com.ogbrett.testprojects.tictactoe.server.connector;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.server.RequestProcessor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

public class JMSConnector implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(JMSConnector.class);

    private final BrokerService broker;
    private final Connection connection;
    private final Session session;
    private final MessageConsumer consumer;
    private final MessageProducer producer;

    JMSConnector(String uri, String queueName, RequestProcessor requestProcessor) throws Exception {
        logger.info("Starting JMSConnector");
        broker = new BrokerService();
        broker.setBrokerName("TicTacToeJMSBroker");
        broker.addConnector(uri);
        broker.start();
        ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory(uri);
        connFactory.setTrustedPackages(Collections.singletonList("com.ogbrett.testprojects.tictactoe"));
        connection = connFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(session.createQueue(queueName));
        producer = session.createProducer(null);
        consumer.setMessageListener(requestMessage -> {
            try {
                TTTRequest request = (TTTRequest)((ObjectMessage)requestMessage).getObject();
                ObjectMessage responseMessage = session.createObjectMessage();
                responseMessage.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
                responseMessage.setObject(requestProcessor.processRequest(request));
                producer.send(requestMessage.getJMSReplyTo(), responseMessage);
            } catch (JMSException e) {
                logger.warn(e.getMessage());
            }
        });
        connection.start();
        logger.info("JMSConnector started");
    }

    @Override
    public void close() throws IOException {
        try {
            logger.info("Stopping JMSConnector");
            connection.stop();
            consumer.close();
            producer.close();
            session.close();
            connection.close();
            broker.stop();
            logger.info("JMSConnector stopped");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

    }
}
