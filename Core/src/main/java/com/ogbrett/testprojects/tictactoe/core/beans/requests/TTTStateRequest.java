package com.ogbrett.testprojects.tictactoe.core.beans.requests;

public class TTTStateRequest extends TTTRequest {
    TTTStateRequest(String login) {
        super(login);
        this.requestType = RequestType.STATE;
    }
}
