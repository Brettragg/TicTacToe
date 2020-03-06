package com.ogbrett.testprojects.tictactoe.core.beans.requests;

public abstract class TTTRequest {
    RequestType requestType;
    private String login;

    TTTRequest(String login) {
        this.login = login;
    }

    public final RequestType getRequestType() {
        return requestType;
    }

    public enum RequestType {
        CONNECTION, MARK, STATE
    }
}
