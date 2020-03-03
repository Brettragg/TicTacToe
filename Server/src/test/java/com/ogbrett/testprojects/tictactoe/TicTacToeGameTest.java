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
        try {
            game.markO(0, 0);
            fail();
        } catch (TTTException e) {
            assertEquals(TTTException.WRONG_TURN, e.getMessage());
        }
    }

    @Test
    public void testAlreadyMarked() throws TTTException {
        game.markX(2, 2);
        try {
            game.markO(2, 2);
            fail();
        } catch (TTTException e) {
            assertEquals(TTTException.ALREADY_MARKED, e.getMessage());
        }
    }
}
