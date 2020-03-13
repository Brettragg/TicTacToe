package com.ogbrett.testprojects.tictactoe.core.beans.responses;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;

public class TTTStateResponse extends TTTResponse {
    private PlayerState playerState;

    public TTTStateResponse(TTTRequest request, Status status, PlayerState playerState) {
        super(request, status);
        this.playerState = playerState;
    }


    public PlayerState getPlayerState() {
        return playerState;
    }

    public enum PlayerState {
        WAITING_FOR_SECOND_PLAYER, YOUR_TURN, OPPONENTS_TURN, WON, LOST
    }
}
