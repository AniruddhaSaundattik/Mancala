package com.bol.mancala.service;

import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.model.PlayerDTO;
import com.bol.mancala.model.TurnDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface TurnService {

    void doTurn(TurnDTO turnDTO);
    void transferStones(TurnDTO turnDTO);
    void transferToBigPitIfEmpty(TurnDTO turnDTO);

}
