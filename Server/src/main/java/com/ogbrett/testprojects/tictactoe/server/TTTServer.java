package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.gamelogic.TTTException;
import javafx.util.Pair;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class TTTServer implements StartStoppable, AutoCloseable {

    private BrokerService brokerService;
    private boolean started;
    private final Connection connection;
    private final List<Pair<Session, MessageConsumer>> sessionConsumerList;

    TTTServer(String uri) throws Exception {
        started = false;
        brokerService = new BrokerService();
        brokerService.setBrokerName("TTTServerBroker");
        brokerService.addConnector(uri);
        brokerService.start();
        connection = new ActiveMQConnectionFactory(uri).createConnection();
        sessionConsumerList = new ArrayList<>();
    }

    @Override
    public void start() throws Exception {
        if (started) {
            throw new TTTException(TTTException.SERVER_ALREADY_STARTED);
        }
        connection.start();
        started = true;
    }

    @Override
    public void stop() throws Exception {
        if (!started) {
            throw new TTTException(TTTException.SERVER_ALREADY_STOPPED);
        }
        connection.stop();
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    void addMessageResponder(MessageResponder messageResponder, Destination destination) throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer messageConsumer = session.createConsumer(destination, messageResponder.getMessageSelector());
        sessionConsumerList.add(new Pair<>(session, messageConsumer));
        messageResponder.setSession(session);
        messageConsumer.setMessageListener(messageResponder);
    }

    @Override
    public void close() throws Exception {
        clearMessageResponders();
        connection.close();
        brokerService.stop();
    }

    void clearMessageResponders() throws JMSException {
        for (Pair<Session, MessageConsumer> pair : sessionConsumerList) {
            pair.getValue().close();
            pair.getKey().close();
        }
        sessionConsumerList.clear();
    }
}
