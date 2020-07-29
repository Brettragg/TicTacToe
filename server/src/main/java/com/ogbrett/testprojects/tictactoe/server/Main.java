package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGame;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGameImpl;
import com.ogbrett.testprojects.tictactoe.server.connector.JMSServerConnector;

public class Main {
    public static void main(String[] args) throws Exception {
        TicTacToeGame game = new TicTacToeGameImpl();
        JMSServerConnector jmsServerConnector = new JMSServerConnector("tcp://localhost:61617", new RequestProcessorImpl(game));
        Thread.sleep(120 * 1000);
        jmsServerConnector.close();
    }
}
