package com.ogbrett.testprojects.tictactoe.gamelogic;

public class TTTException extends Exception {

    static String WRONG_TURN = "Trying to mark on someone else's turn";
    static String ALREADY_MARKED = "The spot you want to mark is already marked";
    static String SQUARE_NOT_EXISTS = "The spot you want to mark does not exist";
    static String GAME_ENDED = "The game has already ended";

    public static String SERVER_ALREADY_STARTED = "The server has already started";
    public static String SERVER_ALREADY_STOPPED = "The server has already stopped";

    public TTTException(String message) {
        super(message);
    }
}