package com.ogbrett.testprojects.tictactoe;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TicTacToeGameTest {
    private TicTacToeGame game;

    @Before
    public void setUp() {
        game = new TicTacToeGame();
    }

    @Test
    public void testTurnState() throws TTTException {
        assertEquals(GameState.X_TURN, game.getState());
        game.markX(1, 1);
        assertEquals(GameState.O_TURN, game.getState());
        game.markO(1, 2);
        assertEquals(GameState.X_TURN, game.getState());
        game.markX(2, 2);
        assertEquals(GameState.O_TURN, game.getState());
        game.markO(0, 2);
        assertEquals(GameState.X_TURN, game.getState());
    }

    @Test
    public void testWrongTurn() {
        testMarkOException(0, 0, TTTException.WRONG_TURN);
    }

    @Test
    public void testAlreadyMarked() throws TTTException {
        game.markX(2, 2);
        testMarkOException(2, 2, TTTException.ALREADY_MARKED);
    }

    @Test
    public void testSquareNotExists() throws TTTException {
        game.markX(2, 2);

        testMarkOException(3, 2, TTTException.SQUARE_NOT_EXISTS);
        testMarkOException(2, 3, TTTException.SQUARE_NOT_EXISTS);
        testMarkOException(-1, 0, TTTException.SQUARE_NOT_EXISTS);
    }

    @Test
    public void xWinMainDiagonal() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 1);
        game.markO(0, 2);
        game.markX(2, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinMainDiagonal() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(0, 2);
        game.markO(1, 1);
        game.markX(1, 2);
        game.markO(2, 2);
        assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinAntiDiagonal() throws TTTException {
        game.markX(2, 0);
        game.markO(1, 0);
        game.markX(1, 1);
        game.markO(0, 0);
        game.markX(0, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinAntiDiagonal() throws TTTException {
        game.markX(2, 2);
        game.markO(2, 0);
        game.markX(0, 0);
        game.markO(1, 1);
        game.markX(1, 2);
        game.markO(0, 2);
        assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinFirstRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 0);
        game.markO(0, 2);
        game.markX(2, 0);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinSecondRow() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(1, 1);
        game.markO(0, 2);
        game.markX(2, 1);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinThirdRow() throws TTTException {
        game.markX(0, 2);
        game.markO(0, 0);
        game.markX(1, 2);
        game.markO(0, 1);
        game.markX(2, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinFirstRow() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(1, 1);
        game.markO(1, 0);
        game.markX(2, 2);
        game.markO(2, 0);
        assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void oWinSecondRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 0);
        game.markO(1, 1);
        game.markX(2, 2);
        game.markO(2, 1);
        assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void oWinThirdRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 2);
        game.markX(1, 0);
        game.markO(1, 2);
        game.markX(2, 1);
        game.markO(2, 2);
        assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinFirstColumn() throws TTTException {
        game.markX(0, 0);
        game.markO(1, 2);
        game.markX(0, 1);
        game.markO(1, 0);
        game.markX(0, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinSecondColumn() throws TTTException {
        game.markX(1, 0);
        game.markO(0, 2);
        game.markX(1, 1);
        game.markO(2, 0);
        game.markX(1, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinThirdColumn() throws TTTException {
        game.markX(2, 0);
        game.markO(0, 2);
        game.markX(2, 1);
        game.markO(1, 0);
        game.markX(2, 2);
        assertEquals(GameState.X_WON, game.getState());
    }

    private void testMarkOException(int x, int y, String expectedExceptionMessage) {
        try {
            game.markO(x, y);
            fail();
        } catch (TTTException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}
