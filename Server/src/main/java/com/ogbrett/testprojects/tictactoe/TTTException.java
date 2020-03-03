package com.ogbrett.testprojects.tictactoe;

class TTTException extends Exception {

    static String WRONG_TURN = "Trying to mark on someone else's turn";
    static String ALREADY_MARKED = "The spot you want to mark is already marked";

    TTTException(String message) {
        super(message);
    }
}