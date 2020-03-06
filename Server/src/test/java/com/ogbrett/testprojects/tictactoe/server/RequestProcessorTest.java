package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTConnectionRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTMarkRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTStateRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestProcessorTest {
    private RequestProcessor requestProcessor;

    @Before
    public void setUp() {
        requestProcessor = new RequestProcessor();
    }

    @Test
    public void testMark() {
        connectionRequest("123123");
        markRequest("123123", 1, 1, TTTResponse.Status.OK);
    }

    @Test
    public void testStates() {
        connectionRequest("123123");
        stateRequest("123123", TTTStateResponse.PlayerState.YOUR_TURN);
        markRequest("123123", 1, 1, TTTResponse.Status.OK);
        stateRequest("123123", TTTStateResponse.PlayerState.OPPONENTS_TURN);
    }

    @Test
    public void testMarkError() {
        connectionRequest("123123");
        markRequest("123123", 4, 100, TTTResponse.Status.ERROR); //out of gamefield
    }

    @Test
    public void testP1Win() {
        connectionRequest("login1");
        connectionRequest("login2");
        markRequest("login1", 0, 0, TTTResponse.Status.OK);
        markRequest("login2", 1, 2, TTTResponse.Status.OK);
        markRequest("login1", 1, 1, TTTResponse.Status.OK);
        markRequest("login2", 2, 1, TTTResponse.Status.OK);
        markRequest("login1", 2, 2, TTTResponse.Status.OK);
        stateRequest("login1", TTTStateResponse.PlayerState.WON);
        stateRequest("login2", TTTStateResponse.PlayerState.LOST);
    }

    @Test
    public void testP2Win() {
        connectionRequest("login1");
        connectionRequest("login2");
        markRequest("login1", 0, 1, TTTResponse.Status.OK);
        markRequest("login2", 0, 0, TTTResponse.Status.OK);
        markRequest("login1", 1, 0, TTTResponse.Status.OK);
        markRequest("login2", 1, 1, TTTResponse.Status.OK);
        markRequest("login1", 2, 1, TTTResponse.Status.OK);
        markRequest("login2", 2, 2, TTTResponse.Status.OK);
        stateRequest("login1", TTTStateResponse.PlayerState.LOST);
        stateRequest("login2", TTTStateResponse.PlayerState.WON);
    }


    private void connectionRequest(String login) {
        TTTRequest request = new TTTConnectionRequest(login);
        TTTResponse response = requestProcessor.processRequest(request);
        assertEquals(TTTResponse.Status.OK, response.getStatus());
        assertEquals(request, response.getRequest());
    }

    private void markRequest(String login, int x, int y, TTTResponse.Status expectedStatus) {
        TTTRequest markRequest = new TTTMarkRequest(login, x, y);
        TTTResponse markResponse = requestProcessor.processRequest(markRequest);
        assertEquals(markRequest, markResponse.getRequest());
        assertEquals(expectedStatus, markResponse.getStatus());
    }

    private void stateRequest(String login, TTTStateResponse.PlayerState expectedState) {
        TTTRequest stateRequest = new TTTStateRequest(login);
        TTTStateResponse stateResponse = (TTTStateResponse)requestProcessor.processRequest(stateRequest);
        assertEquals(stateRequest, stateResponse.getRequest());
        assertEquals(TTTResponse.Status.OK, stateResponse.getStatus());
        assertEquals(expectedState, stateResponse.getPlayerState());
    }
}
