package com.ogbrett.testprojects.tictactoe.server;

import javax.jms.MessageListener;
import javax.jms.Session;

public interface MessageResponder extends MessageListener {
    void setSession(Session session);
    String getMessageSelector();
}
