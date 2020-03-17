package com.ogbrett.testprojects.tictactoe.server.mocks;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.server.RequestProcessor;

public class RequestProcessorMock implements RequestProcessor {
    @Override
    public TTTResponse processRequest(TTTRequest request) {
        return new TTTResponse(request, TTTResponse.Status.OK);
    }
}
