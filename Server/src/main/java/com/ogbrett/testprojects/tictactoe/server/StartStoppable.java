package com.ogbrett.testprojects.tictactoe.server;

public interface StartStoppable {
    void start() throws Exception;

    void stop() throws Exception;

    boolean isStarted();
}
