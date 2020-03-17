package com.ogbrett.testprojects.tictactoe.server.connector;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.server.RequestProcessor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;

public class JMSServerConnector implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(JMSServerConnector.class);

    private final BrokerService broker;
    private final Connection connection;
    private final Session session;
    private final MessageConsumer consumer;
    private final MessageProducer producer;

    public JMSServerConnector(String uri, String queueName, RequestProcessor requestProcessor) throws Exception {
        logger.info("Starting JMSServerConnector");
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
                if (requestMessage.getJMSReplyTo() == null) {
                    throw new JMSException("Reply destination isn't specified");
                }
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
        logger.info("JMSServerConnector started");
    }

    @Override
    public void close() throws IOException {
        try {
            logger.info("Stopping JMSServerConnector");
            connection.stop();
            consumer.close();
            producer.close();
            session.close();
            connection.close();
            broker.stop();
            logger.info("JMSServerConnector stopped");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
