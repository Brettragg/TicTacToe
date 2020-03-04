package com.ogbrett.testprojects.tictactoe.gamelogic;


class TicTacToeGameImpl implements TicTacToeGame {
    private static final int DIMENSION = 3;
    private static final int X_MARK = 1;
    private static final int O_MARK = 2;
    private GameState gameState;
    private int[][] gameField;

    TicTacToeGameImpl() {
        gameState = GameState.X_TURN;
        gameField = new int[DIMENSION][DIMENSION];
    }

    public GameState getState() {
        return gameState;
    }

    public void markX(int x, int y) throws TTTException {
        validateMarkInput(x, y, GameState.X_TURN);
        gameField[x][y] = X_MARK;
        if (won(X_MARK)) {
            gameState = GameState.X_WON;
        } else if (noMoreFieldSpace()) {
            gameState = GameState.TIE;
        } else {
            gameState = GameState.O_TURN;
        }
    }

    public void markO(int x, int y) throws TTTException{
        validateMarkInput(x, y, GameState.O_TURN);
        gameField[x][y] = O_MARK;
        if (won(O_MARK)) {
            gameState = GameState.O_WON;
        } else {
            gameState = GameState.X_TURN;
        }
    }

    private void validateMarkInput(int x, int y, GameState expectedState) throws TTTException {
        if (gameState == GameState.X_WON || gameState == GameState.O_WON || gameState == GameState.TIE) {
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

    private boolean won(int expectedMarks) {
        boolean wonColumnOrRow = false;
        boolean wonMainDiagonal = true;
        boolean wonAntiDiagonal = true;
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
            wonColumnOrRow = wonColumnOrRow || localWonRow || localWonColumn;
            if (gameField[i][i] != expectedMarks) {
                wonMainDiagonal = false;
            }
            if (gameField[i][DIMENSION - i - 1] != expectedMarks) {
                wonAntiDiagonal = false;
            }
        }
        return wonColumnOrRow || wonMainDiagonal || wonAntiDiagonal;
    }

    private boolean noMoreFieldSpace() {
        boolean gotSpace = false;
        for (int i = 0; i < DIMENSION; ++i) {
            for (int j = 0; j < DIMENSION; ++j) {
                if (gameField[i][j] != X_MARK && gameField[i][j] != O_MARK) {
                    gotSpace = true;
                }
            }
        }
        return !gotSpace;
    }
}