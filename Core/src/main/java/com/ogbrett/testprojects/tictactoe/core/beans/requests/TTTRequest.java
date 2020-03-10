package com.ogbrett.testprojects.tictactoe.core.beans.requests;

import java.io.Serializable;

public abstract class TTTRequest implements Serializable {
    RequestType requestType;
    private String login;

    TTTRequest(String login) {
        this.login = login;
    }

    public final RequestType getRequestType() {
        return requestType;
    }
    public String getLogin() {
        return login;
    }

    public enum RequestType {
        CONNECTION, MARK, STATE
    }
}
