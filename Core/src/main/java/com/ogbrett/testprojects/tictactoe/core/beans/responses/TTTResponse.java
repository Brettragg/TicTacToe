package com.ogbrett.testprojects.tictactoe.core.beans.responses;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;

public class TTTResponse {
    private TTTRequest request;
    private Status status;
    TTTResponse(TTTRequest request, Status status) {
        this.request = request;
        this.status = status;
    }

    public TTTRequest getRequest() {
        return request;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        OK, ERROR
    }
}
