package com.ogbrett.testprojects.tictactoe;


class TicTacToeGame {
    private GameState gameState;
    private int[][] gameField;

    TicTacToeGame() {
        gameState = GameState.X_TURN;
        gameField = new int[3][3];
    }

    void markX(int x, int y) throws TTTException{
        if (gameState != GameState.X_TURN) {
            throw new TTTException(TTTException.WRONG_TURN);
        }
        if (gameField[x][y] != 0) {
            throw new TTTException(TTTException.ALREADY_MARKED);
        }
        gameField[x][y] = 1;
        gameState = GameState.O_TURN;
    }

    void markO(int x, int y) throws TTTException{
        if (gameState != GameState.O_TURN) {
            throw new TTTException(TTTException.WRONG_TURN);
        }
        if (gameField[x][y] != 0) {
            throw new TTTException(TTTException.ALREADY_MARKED);
        }
        gameField[x][y] = 1;
        gameState = GameState.X_TURN;
    }

    GameState getState() {
        return gameState;
    }
}
