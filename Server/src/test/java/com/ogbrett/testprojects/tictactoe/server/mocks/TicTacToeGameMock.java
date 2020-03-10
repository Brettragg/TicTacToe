package com.ogbrett.testprojects.tictactoe.server.mocks;

import com.ogbrett.testprojects.tictactoe.core.gamelogic.GameState;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TTTException;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGame;

public class TicTacToeGameMock implements TicTacToeGame {
    private GameState state;
    private boolean firstTurn;

    public TicTacToeGameMock() {
        state = GameState.X_TURN;
        firstTurn = true;
    }

    @Override
    public void markX(int x, int y) throws TTTException {
        if (x == 3 && y == 3) {
            throw new TTTException(TTTException.SQUARE_NOT_EXISTS);
        }
        if (state == GameState.X_TURN && firstTurn) {
            state = GameState.O_TURN;
        } else {
            state = GameState.X_WON;
        }
    }

    @Override
    public void markO(int x, int y) {
        state = GameState.X_TURN;
        firstTurn = false;
    }

    @Override
    public GameState getState() {
        return state;
    }
}
