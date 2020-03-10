package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTConnectionRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTMarkRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTStateRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;
import com.ogbrett.testprojects.tictactoe.server.mocks.TicTacToeGameMock;
import org.junit.Before;
import org.junit.Test;

import static com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse.*;
import static com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse.*;
import static org.junit.Assert.assertEquals;

public class RequestProcessorTest {
    private RequestProcessor requestProcessor;

    @Before
    public void setUp() {
        requestProcessor = new RequestProcessor(new TicTacToeGameMock());
    }

    @Test
    public void testMark() {
        connectionRequest("123123", Status.OK);
        markRequest("123123", 1, 1, Status.OK);
    }

    @Test
    public void testStates() {
        connectionRequest("123123", Status.OK);
        stateRequest("123123", PlayerState.YOUR_TURN);
        markRequest("123123", 1, 1, Status.OK);
        stateRequest("123123", PlayerState.OPPONENTS_TURN);
    }

    @Test
    public void testMarkError() {
        connectionRequest("123123", Status.OK);
        markRequest("123123", 3, 3, Status.ERROR); //out of gamefield
    }

    @Test
    public void testWin() {
        connectionRequest("login1", Status.OK);
        connectionRequest("login2", Status.OK);
        markRequest("login1", 0, 0, Status.OK);
        markRequest("login2", 1, 2, Status.OK);
        markRequest("login1", 0, 1, Status.OK);
        stateRequest("login1", PlayerState.WON);
        stateRequest("login2", PlayerState.LOST);
    }

    @Test
    public void testSameLogin() {
        connectionRequest("login1", Status.OK);
        connectionRequest("login1", Status.ERROR);
    }


    private void connectionRequest(String login, Status expectedStatus) {
        TTTRequest request = new TTTConnectionRequest(login);
        TTTResponse response = requestProcessor.processRequest(request);
        assertEquals(expectedStatus, response.getStatus());
        assertEquals(request, response.getRequest());
    }

    private void markRequest(String login, int x, int y, Status expectedStatus) {
        TTTRequest markRequest = new TTTMarkRequest(login, x, y);
        TTTResponse markResponse = requestProcessor.processRequest(markRequest);
        assertEquals(markRequest, markResponse.getRequest());
        assertEquals(expectedStatus, markResponse.getStatus());
    }

    private void stateRequest(String login, PlayerState expectedState) {
        TTTRequest stateRequest = new TTTStateRequest(login);
        TTTStateResponse stateResponse = (TTTStateResponse)requestProcessor.processRequest(stateRequest);
        assertEquals(stateRequest, stateResponse.getRequest());
        assertEquals(Status.OK, stateResponse.getStatus());
        assertEquals(expectedState, stateResponse.getPlayerState());
    }
}
