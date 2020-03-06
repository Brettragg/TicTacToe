package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTMarkRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.GameState;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TTTException;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGame;
import com.ogbrett.testprojects.tictactoe.core.gamelogic.TicTacToeGameImpl;

import java.util.Objects;

class RequestProcessor {
    private TicTacToeGame game = new TicTacToeGameImpl();
    private String login1;
    private String login2;
    TTTResponse processRequest(TTTRequest request) {
        switch (request.getRequestType()) {
            case CONNECTION:
                if (login1 == null) {
                    login1 = request.getLogin();
                } else {
                    login2 = request.getLogin();
                }
                return new TTTResponse(request, TTTResponse.Status.OK);
            case STATE:                 // TODO: 06.03.2020 refactor this!!! you could probably divide x and o players at start
                if (xTurn(game) && isXPlayer(request)|| oTurn(game) && isOPlayer(request)) {
                    return new TTTStateResponse(request, TTTResponse.Status.OK, TTTStateResponse.PlayerState.YOUR_TURN);
                } else if (oTurn(game) && isXPlayer(request) || xTurn(game) && isOPlayer(request)) {
                    return new TTTStateResponse(request, TTTResponse.Status.OK, TTTStateResponse.PlayerState.OPPONENTS_TURN);
                } else if (game.getState() == GameState.X_WON && isXPlayer(request) || game.getState() == GameState.O_WON && isOPlayer(request)) {
                    return new TTTStateResponse(request, TTTResponse.Status.OK, TTTStateResponse.PlayerState.WON);
                } else if (game.getState() == GameState.X_WON && isOPlayer(request) || game.getState() == GameState.O_WON && isXPlayer(request)) {
                    return new TTTStateResponse(request, TTTResponse.Status.OK, TTTStateResponse.PlayerState.LOST);
                } else {
                    return new TTTStateResponse(request, TTTResponse.Status.ERROR, null);
                }
            case MARK:
                try {
                    if (isXPlayer(request)) {
                        game.markX(((TTTMarkRequest) request).getX(), ((TTTMarkRequest) request).getY());
                    } else if (isOPlayer(request)) {
                        game.markO(((TTTMarkRequest) request).getX(), ((TTTMarkRequest) request).getY());
                    }
                    return new TTTResponse(request, TTTResponse.Status.OK);
                } catch (TTTException e) {
                    return new TTTResponse(request, TTTResponse.Status.ERROR);
                }

            default:
                throw new RuntimeException("Unsupported request type");
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
