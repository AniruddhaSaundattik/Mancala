package com.bol.mancala.service;

import com.bol.mancala.model.GameStartDTO;
import com.bol.mancala.model.MancalaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


public interface GameService {

    MancalaDTO startGame(GameStartDTO gameStartDTO);
    void executeGameOver(MancalaDTO mancalaObj);
    boolean checkGameOver(MancalaDTO mancalaObj);
}
