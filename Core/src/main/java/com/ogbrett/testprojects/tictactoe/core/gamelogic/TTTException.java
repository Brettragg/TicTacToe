package com.ogbrett.testprojects.tictactoe.core.gamelogic;

class TTTException extends Exception {

    static String WRONG_TURN = "Trying to mark on someone else's turn";
    static String ALREADY_MARKED = "The spot you want to mark is already marked";
    static String SQUARE_NOT_EXISTS = "The spot you want to mark does not exist";
    static String GAME_ENDED = "The game has already ended";

    TTTException(String message) {
        super(message);
    }
}