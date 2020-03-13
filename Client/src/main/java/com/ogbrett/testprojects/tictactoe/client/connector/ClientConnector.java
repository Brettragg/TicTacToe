package com.ogbrett.testprojects.tictactoe.client.connector;

import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;

import java.io.Closeable;

interface ClientConnector extends Closeable {
    void mark(int x, int y) throws Exception;
    TTTStateResponse.PlayerState getState() throws Exception;
}
