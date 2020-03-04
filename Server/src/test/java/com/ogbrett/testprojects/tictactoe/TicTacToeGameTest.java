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

    private void testMarkOException(int x, int y, String expectedExceptionMessage) {
        try {
            game.markO(x, y);
            fail();
        } catch (TTTException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }
}
