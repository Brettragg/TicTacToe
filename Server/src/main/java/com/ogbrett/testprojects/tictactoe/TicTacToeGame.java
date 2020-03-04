package com.ogbrett.testprojects.tictactoe;


class TicTacToeGame {
    private static final int DIMENSION = 3;
    private static final int X_MARK = 1;
    private static final int O_MARK = 2;
    private GameState gameState;
    private int[][] gameField;

    TicTacToeGame() {
        gameState = GameState.X_TURN;
        gameField = new int[DIMENSION][DIMENSION];
    }

    void markX(int x, int y) throws TTTException{
        validateMarkInput(x, y, GameState.X_TURN);
        gameField[x][y] = X_MARK;
        if (diagonalWon(X_MARK) || columnOrRowWon(X_MARK)) {
            gameState = GameState.X_WON;
        } else {
            gameState = GameState.O_TURN;
        }
    }

    void markO(int x, int y) throws TTTException{
        validateMarkInput(x, y, GameState.O_TURN);
        gameField[x][y] = O_MARK;
        if (diagonalWon(O_MARK) || columnOrRowWon(O_MARK)) {
            gameState = GameState.O_WON;
        } else {
            gameState = GameState.X_TURN;
        }
    }

    private void validateMarkInput(int x, int y, GameState expectedState) throws TTTException {
        if (gameState == GameState.X_WON || gameState == GameState.O_WON) {
            throw new TTTException(TTTException.GAME_ENDED);
        }
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

    private boolean diagonalWon(int expectedMarks) {
        boolean localWonMain = true;
        boolean localWonAnti = true;
        for (int i = 0; i < DIMENSION; ++i) {
            if (gameField[i][i] != expectedMarks) {
                localWonMain = false;
            }
            if (gameField[i][DIMENSION - i - 1] != expectedMarks) {
                localWonAnti = false;
            }
        }
        return localWonMain || localWonAnti;
    }

    private boolean columnOrRowWon(int expectedMarks) {
        boolean globalWon = false;
        for (int i = 0; i < DIMENSION; ++i) {
            boolean localWonColumn = true;
            boolean localWonRow = true;
            for (int j = 0; j < DIMENSION; ++j) {
                if (gameField[i][j] != expectedMarks) {
                    localWonColumn = false;
                }
                if (gameField[j][i] != expectedMarks) {
                    localWonRow = false;
                }
            }
            globalWon = globalWon || localWonRow || localWonColumn;
        }
        return globalWon;
    }

    GameState getState() {
        return gameState;
    }
}
