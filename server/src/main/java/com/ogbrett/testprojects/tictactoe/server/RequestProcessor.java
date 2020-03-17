package com.ogbrett.testprojects.tictactoe.server;

import com.ogbrett.testprojects.tictactoe.core.beans.requests.TTTRequest;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTResponse;

public interface RequestProcessor {
    TTTResponse processRequest(TTTRequest request);
}
