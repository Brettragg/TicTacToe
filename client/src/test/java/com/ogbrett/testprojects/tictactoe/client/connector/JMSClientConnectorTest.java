package com.ogbrett.testprojects.tictactoe.client.connector;

import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGameImpl;
import com.ogbrett.testprojects.tictactoe.server.RequestProcessorImpl;
import com.ogbrett.testprojects.tictactoe.server.connector.JMSServerConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.JMSException;
import java.io.Closeable;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JMSClientConnectorTest {
    private Closeable jmsServer;
    private static final String URI = "tcp://localhost:61618";

    @Before
    public void setUp() throws Exception {
        jmsServer = new JMSServerConnector(URI, new RequestProcessorImpl(new TicTacToeGameImpl()));
    }
    @After
    public void tearDown() throws IOException {
        jmsServer.close();
    }
    @Test
    public void integrationTest() throws Exception {
        ClientConnector clientConnectorX = new JMSClientConnector(URI, "login1");
        assertEquals(TTTStateResponse.PlayerState.WAITING_FOR_SECOND_PLAYER, clientConnectorX.getState());
        ClientConnector clientConnectorO = new JMSClientConnector(URI, "login2");
        assertEquals(TTTStateResponse.PlayerState.YOUR_TURN, clientConnectorX.getState());
        clientConnectorX.mark(0, 0);
        assertEquals(TTTStateResponse.PlayerState.OPPONENTS_TURN, clientConnectorX.getState());
        clientConnectorO.mark(1, 2);
        try {
            clientConnectorX.mark(1, 2);
            fail();
        } catch (JMSException ignored) {
        }
        clientConnectorX.mark(1, 1);
        clientConnectorO.mark(2, 1);
        clientConnectorX.mark(2, 2);
        assertEquals(TTTStateResponse.PlayerState.WON, clientConnectorX.getState());
        assertEquals(TTTStateResponse.PlayerState.LOST, clientConnectorO.getState());
        try {
            clientConnectorX.mark(0, 1);
            fail();
        } catch (JMSException ignored) {
        }
        clientConnectorX.close();
        clientConnectorO.close();

    }
}
