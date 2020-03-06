package com.ogbrett.testprojects.tictactoe.core.gamelogic;

public interface TicTacToeGame {
    void markX(int x, int y) throws TTTException;
    void markO(int x, int y) throws TTTException;
    GameState getState();
}
