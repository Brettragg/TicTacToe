package com.ogbrett.testprojects.tictactoe.gamelogic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestExceptionThrows {
    private TicTacToeGame game;

    @Before
    public void setUp() {
        game = new TicTacToeGameImpl();
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
    public void testMarkXGameWonException() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(1, 0);
        game.markO(1, 1);
        game.markX(2, 0);
        game.markO(2, 2);
        try {
            game.markX(1, 2);
            fail();
        } catch (TTTException e) {
            assertEquals(TTTException.GAME_ENDED, e.getMessage());
        }
    }

    @Test
    public void testMarkOGameWonException() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 1);
        game.markO(1, 0);
        game.markX(2, 2);
        testMarkOException(1, 2, TTTException.GAME_ENDED);
    }

    @Test
    public void testMarkXGameTiedException() throws TTTException {
        game.markX(0, 0);
        game.markO(2, 0);
        game.markX(1, 0);
        game.markO(0, 1);
        game.markX(1, 1);
        game.markO(1, 2);
        game.markX(2, 1);
        game.markO(2, 2);
        game.markX(0, 2);
        testMarkOException(0, 0, TTTException.GAME_ENDED);
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
