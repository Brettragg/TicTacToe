package com.ogbrett.testprojects.tictactoe.core.beans.requests;

public class TTTMarkRequest extends TTTRequest {
    private int x;
    private int y;

    TTTMarkRequest(String login, int x, int y) {
        super(login);
        this.x = x;
        this.y = y;
        requestType = RequestType.MARK;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
