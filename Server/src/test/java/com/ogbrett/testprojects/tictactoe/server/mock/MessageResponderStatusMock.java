package com.ogbrett.testprojects.tictactoe.server.mock;

import com.ogbrett.testprojects.tictactoe.server.MessageResponder;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class MessageResponderStatusMock implements MessageResponder {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String getMessageSelector() {
        return "Request = 'Status'";
    }

    @Override
    public void onMessage(Message message) {
        try {
            if ("Status".equals(message.getStringProperty("Request"))) {
                Message replyMessage = new ActiveMQMessage();
                replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                replyMessage.setStringProperty("Status","OK");
                MessageProducer messageProducer = session.createProducer(message.getJMSReplyTo());
                messageProducer.send(replyMessage);
                messageProducer.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
