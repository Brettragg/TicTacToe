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

class RequestProcessor {
    private TicTacToeGame game = new TicTacToeGameImpl();
    private String login1;
    private String login2;
    TTTResponse processRequest(TTTRequest request) {
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
        if (xTurn(game) && isXPlayer(request)|| oTurn(game) && isOPlayer(request)) {
            return new TTTStateResponse(request, Status.OK, PlayerState.YOUR_TURN);
        } else if (oTurn(game) && isXPlayer(request) || xTurn(game) && isOPlayer(request)) {
            return new TTTStateResponse(request, Status.OK, PlayerState.OPPONENTS_TURN);
        } else if (game.getState() == GameState.X_WON && isXPlayer(request) || game.getState() == GameState.O_WON && isOPlayer(request)) {
            return new TTTStateResponse(request, Status.OK, PlayerState.WON);
        } else if (game.getState() == GameState.X_WON && isOPlayer(request) || game.getState() == GameState.O_WON && isXPlayer(request)) {
            return new TTTStateResponse(request, Status.OK, PlayerState.LOST);
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
