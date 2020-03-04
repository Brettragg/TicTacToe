package com.ogbrett.testprojects.tictactoe;


class TicTacToeGame {
    private final int DIMENSION = 3;

    private GameState gameState;
    private int[][] gameField;

    TicTacToeGame() {
        gameState = GameState.X_TURN;
        gameField = new int[DIMENSION][DIMENSION];
    }

    void markX(int x, int y) throws TTTException{
        validateMarkInput(x, y, GameState.X_TURN);
        gameField[x][y] = 1;
        gameState = GameState.O_TURN;
    }

    void markO(int x, int y) throws TTTException{
        validateMarkInput(x, y, GameState.O_TURN);
        gameField[x][y] = 1;
        gameState = GameState.X_TURN;
    }

    private void validateMarkInput(int x, int y, GameState expectedState) throws TTTException {
        if (gameState != expectedState) {
            throw new TTTException(TTTException.WRONG_TURN);
        }
        if (x >= DIMENSION || y >= DIMENSION || x < 0 || y < 0) {
            throw new TTTException(TTTException.SQUARE_NOT_EXISTS);
        }
        if (gameField[x][y] != 0) {
            throw new TTTException(TTTException.ALREADY_MARKED);
        }
    }

    GameState getState() {
        return gameState;
    }
}
