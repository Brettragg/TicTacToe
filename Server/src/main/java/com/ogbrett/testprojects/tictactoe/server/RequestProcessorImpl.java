package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTConnectionRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTMarkRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTStateRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.GameState;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TTTException;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGame;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGameImpl;

import java.util.Objects;

import static com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse.*;
import static com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse.*;

class RequestProcessorImpl implements RequestProcessor {
    private TicTacToeGame game;
    private String login1;
    private String login2;

    RequestProcessorImpl(TicTacToeGame game) {
        this.game = game;
    }

    public TTTResponse processRequest(TTTRequest request) {
        switch (request.getRequestType()) {
            case CONNECTION:
                return processConnectionRequest((TTTConnectionRequest)request);
            case STATE:
                return processStateRequest((TTTStateRequest)request);
            case MARK:
                return processMarkRequest((TTTMarkRequest)request);
            default:
                throw new RuntimeException("Unsupported request type");
        }
    }

    private TTTResponse processConnectionRequest(TTTConnectionRequest request) {
        if (login1 == null) {
            login1 = request.getLogin();
        } else if (!login1.equals(request.getLogin())){
            login2 = request.getLogin();
        } else {
            return new TTTResponse(request, Status.ERROR);
        }
        return new TTTResponse(request, Status.OK);
    }

    private TTTResponse processStateRequest(TTTStateRequest request) {
        return processStateRequestWithRole(request, isXPlayer(request));
    }

    private TTTResponse processStateRequestWithRole(TTTStateRequest request, boolean isX) {
        if (xTurn(game)) {
            return new TTTStateResponse(request, Status.OK, isX? PlayerState.YOUR_TURN : PlayerState.OPPONENTS_TURN);
        } else if (oTurn(game)) {
            return new TTTStateResponse(request, Status.OK, isX? PlayerState.OPPONENTS_TURN : PlayerState.YOUR_TURN);
        } else if (game.getState() == GameState.X_WON) {
            return new TTTStateResponse(request, Status.OK, isX? PlayerState.WON : PlayerState.LOST);
        } else if (game.getState() == GameState.O_WON) {
            return new TTTStateResponse(request, Status.OK, isX? PlayerState.LOST : PlayerState.WON);
        } else {
            return new TTTStateResponse(request, Status.ERROR, null);
        }
    }

    private TTTResponse processMarkRequest(TTTMarkRequest request) {
        try {
            if (isXPlayer(request)) {
                game.markX(request.getX(), request.getY());
            } else if (isOPlayer(request)) {
                game.markO(request.getX(), request.getY());
            }
            return new TTTResponse(request, Status.OK);
        } catch (TTTException e) {
            return new TTTResponse(request, Status.ERROR);
        }
    }

    private boolean isXPlayer(TTTRequest request) {
        return Objects.equals(login1, request.getLogin());
    }
    private boolean isOPlayer(TTTRequest request) {
        return Objects.equals(login2, request.getLogin());
    }
    private boolean xTurn(TicTacToeGame game) {
        return game.getState() == GameState.X_TURN;
    }
    private boolean oTurn(TicTacToeGame game) {
        return game.getState() == GameState.O_TURN;
    }
}
