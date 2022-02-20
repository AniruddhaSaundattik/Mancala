package com.bol.mancala.service.impl;

import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.model.PlayerDTO;
import com.bol.mancala.model.TurnDTO;
import com.bol.mancala.service.GameService;
import com.bol.mancala.service.TurnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnServiceImpl implements TurnService {
    private static final Logger logger = LoggerFactory.getLogger(TurnService.class);

    @Autowired
    GameService gameService;

    public void doTurn(TurnDTO turnDTO) {
        MancalaDTO mancalaObj = turnDTO.getMancalaObj();
        this.transferStones(turnDTO);
        logger.info("Transfer to big pit if last stone in player's empty pit");
        if (mancalaObj.isLastStoneInEmptyPit())
            this.transferToBigPitIfEmpty(turnDTO);
        logger.info("Checking if game is over");
        if (this.gameService.checkGameOver(mancalaObj)) {
            mancalaObj.setGameOver(true);
            this.gameService.executeGameOver(mancalaObj);
        }
    }

    public void transferStones(TurnDTO turnDTO) {
        MancalaDTO mancalaObj = turnDTO.getMancalaObj();
        PlayerDTO turnPlayer;
        PlayerDTO opponent;
        int stones;
        if (mancalaObj.getPlayer1().getName().equalsIgnoreCase(turnDTO.getPlayer())) {
            turnPlayer = mancalaObj.getPlayer1();
            opponent = mancalaObj.getPlayer2();
        } else {
            turnPlayer = mancalaObj.getPlayer2();
            opponent = mancalaObj.getPlayer1();
        }
        stones = turnPlayer.getSmallPitsCount()[turnDTO.getPitClicked()];

        int[] turnPlayerSmallPitsCount = turnPlayer.getSmallPitsCount();
        turnPlayerSmallPitsCount[turnDTO.getPitClicked()] = 0;

        stones = increaseOwnPitCount(turnPlayer, stones, turnDTO.getPitClicked(), mancalaObj);
        while(stones > 0){
            turnPlayer.setBigPitCount(turnPlayer.getBigPitCount() + 1);
            if (stones == 1)
                mancalaObj.setRepeatTurn(true);
            stones--;
            stones = increaseOpponentPitCount(opponent, stones);
            stones = increaseOwnPitCount(turnPlayer, stones, -1, mancalaObj);
        }
        logger.info("Stones in pit clicked transferred to other pits");
    }

    public void transferToBigPitIfEmpty(TurnDTO turnDTO) {
        MancalaDTO mancalaObj = turnDTO.getMancalaObj();
        PlayerDTO turnPlayer;
        PlayerDTO opponent;
        if (mancalaObj.getPlayer1().getName().equalsIgnoreCase(turnDTO.getPlayer())) {
            turnPlayer = mancalaObj.getPlayer1();
            opponent = mancalaObj.getPlayer2();
        } else {
            turnPlayer = mancalaObj.getPlayer2();
            opponent = mancalaObj.getPlayer1();
        }
        int[] opponentSmallPitsCount = opponent.getSmallPitsCount();
        int[] turnPlayerSmallPitsCount = turnPlayer.getSmallPitsCount();
        int opponentComplimentaryPitNo = opponentSmallPitsCount.length - 1 - mancalaObj.getLastPitNumber();
        if (opponentSmallPitsCount[opponentComplimentaryPitNo] > 0) {
            turnPlayer.setBigPitCount(turnPlayer.getBigPitCount() + opponentSmallPitsCount[opponentComplimentaryPitNo] + 1);
            opponentSmallPitsCount[opponentComplimentaryPitNo] = 0;
            opponent.setSmallPitsCount(opponentSmallPitsCount);
            turnPlayerSmallPitsCount[mancalaObj.getLastPitNumber()] = 0;
            turnPlayer.setSmallPitsCount(turnPlayerSmallPitsCount);
        }
        mancalaObj.setLastStoneInEmptyPit(false);
        mancalaObj.setLastPitNumber(0);
        logger.info("Stones tranferred from player clicked pit and opponent's pit facing clicked pit to player's big pit");
    }

    private int increaseOwnPitCount(PlayerDTO turnPlayer, int stones, int clickedPit, MancalaDTO mancalaObj) {
        int[] smallPitsTurnPlayer = turnPlayer.getSmallPitsCount();
        for (int i = clickedPit + 1;
             i < smallPitsTurnPlayer.length && stones > 0;
             i++) {
            if (stones == 1 && smallPitsTurnPlayer[i]==0) {
                mancalaObj.setLastStoneInEmptyPit(true);
                mancalaObj.setLastPitNumber(i);
            }
            smallPitsTurnPlayer[i] += 1;
            stones--;
        }
        turnPlayer.setSmallPitsCount(smallPitsTurnPlayer);
        return stones;
    }

    private int increaseOpponentPitCount(PlayerDTO opponent, int stones) {
        int[] opponentSmallPits = opponent.getSmallPitsCount();
        for (int i = 0; stones>0 && i<opponentSmallPits.length; i++){
            opponentSmallPits[i] += 1;
            stones--;
        }
        opponent.setSmallPitsCount(opponentSmallPits);
        return stones;
    }
}
