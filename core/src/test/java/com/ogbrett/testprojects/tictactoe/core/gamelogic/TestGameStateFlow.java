package com.ogbrett.testprojects.tictactoe.core.gamelogic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGameStateFlow {
    private TicTacToeGame game;

    @Before
    public void setUp() {
        game = new TicTacToeGameImpl();
    }

    @Test
    public void testTurnState() throws TTTException {
        Assert.assertEquals(GameState.X_TURN, game.getState());
        game.markX(1, 1);
        Assert.assertEquals(GameState.O_TURN, game.getState());
        game.markO(1, 2);
        Assert.assertEquals(GameState.X_TURN, game.getState());
        game.markX(2, 2);
        Assert.assertEquals(GameState.O_TURN, game.getState());
        game.markO(0, 2);
        Assert.assertEquals(GameState.X_TURN, game.getState());
    }

    @Test
    public void xWinMainDiagonal() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 1);
        game.markO(0, 2);
        game.markX(2, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinMainDiagonal() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(0, 2);
        game.markO(1, 1);
        game.markX(1, 2);
        game.markO(2, 2);
        Assert.assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinAntiDiagonal() throws TTTException {
        game.markX(2, 0);
        game.markO(1, 0);
        game.markX(1, 1);
        game.markO(0, 0);
        game.markX(0, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinAntiDiagonal() throws TTTException {
        game.markX(2, 2);
        game.markO(2, 0);
        game.markX(0, 0);
        game.markO(1, 1);
        game.markX(1, 2);
        game.markO(0, 2);
        Assert.assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinFirstRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 0);
        game.markO(0, 2);
        game.markX(2, 0);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinSecondRow() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(1, 1);
        game.markO(0, 2);
        game.markX(2, 1);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinThirdRow() throws TTTException {
        game.markX(0, 2);
        game.markO(0, 0);
        game.markX(1, 2);
        game.markO(0, 1);
        game.markX(2, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void oWinFirstRow() throws TTTException {
        game.markX(0, 1);
        game.markO(0, 0);
        game.markX(1, 1);
        game.markO(1, 0);
        game.markX(2, 2);
        game.markO(2, 0);
        Assert.assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void oWinSecondRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 1);
        game.markX(1, 0);
        game.markO(1, 1);
        game.markX(2, 2);
        game.markO(2, 1);
        Assert.assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void oWinThirdRow() throws TTTException {
        game.markX(0, 0);
        game.markO(0, 2);
        game.markX(1, 0);
        game.markO(1, 2);
        game.markX(2, 1);
        game.markO(2, 2);
        Assert.assertEquals(GameState.O_WON, game.getState());
    }

    @Test
    public void xWinFirstColumn() throws TTTException {
        game.markX(0, 0);
        game.markO(1, 2);
        game.markX(0, 1);
        game.markO(1, 0);
        game.markX(0, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinSecondColumn() throws TTTException {
        game.markX(1, 0);
        game.markO(0, 2);
        game.markX(1, 1);
        game.markO(2, 0);
        game.markX(1, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void xWinThirdColumn() throws TTTException {
        game.markX(2, 0);
        game.markO(0, 2);
        game.markX(2, 1);
        game.markO(1, 0);
        game.markX(2, 2);
        Assert.assertEquals(GameState.X_WON, game.getState());
    }

    @Test
    public void testTie() throws TTTException {
        game.markX(0, 0);
        game.markO(2, 0);
        game.markX(1, 0);
        game.markO(0, 1);
        game.markX(1, 1);
        game.markO(1, 2);
        game.markX(2, 1);
        game.markO(2, 2);
        game.markX(0, 2);
        Assert.assertEquals(GameState.TIE, game.getState());
    }
}
