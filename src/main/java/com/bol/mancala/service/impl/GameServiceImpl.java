package com.bol.mancala.service.impl;

import com.bol.mancala.model.GameStartDTO;
import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    public MancalaDTO startGame(GameStartDTO gameStartDTO) {
        logger.info("Creating new game with provided settings");
        return new MancalaDTO(gameStartDTO.getSmallPits(), gameStartDTO.getSmallPitStonesCount(), gameStartDTO.getPlayer1(),
                gameStartDTO.getPlayer2());
    }

    public void executeGameOver(MancalaDTO mancalaObj) {
        checkGameOver(mancalaObj);
        String winner = "Draw";
        if (mancalaObj.getPlayer1().getScore() > mancalaObj.getPlayer2().getScore())
            winner = mancalaObj.getPlayer1().getName();
        else if (mancalaObj.getPlayer1().getScore() < mancalaObj.getPlayer2().getScore())
            winner = mancalaObj.getPlayer2().getName();
        logger.info("Winner is declared");
        mancalaObj.setWinner(winner);
    }

    public boolean checkGameOver(MancalaDTO mancalaObj) {
        boolean player1Done = true;
        int player1Score = 0;
        boolean player2Done = true;
        int player2Score = 0;
        int[] smallPitsPlayer1 = mancalaObj.getPlayer1().getSmallPitsCount();
        int[] smallPitsPlayer2 = mancalaObj.getPlayer2().getSmallPitsCount();
        for (int i=0; i< mancalaObj.getPlayer1().getSmallPitsCount().length; i++) {
            player1Score += smallPitsPlayer1[i];
            player2Score += smallPitsPlayer2[i];
            if (mancalaObj.getPlayer1().getSmallPitsCount()[i] != 0)
                player1Done = false;
            if (mancalaObj.getPlayer2().getSmallPitsCount()[i] != 0)
                player2Done = false;
        }
        if (player1Done || player2Done) {
            mancalaObj.getPlayer1().setScore(player1Score + mancalaObj.getPlayer1().getBigPitCount());
            mancalaObj.getPlayer2().setScore(player2Score + mancalaObj.getPlayer2().getBigPitCount());
            logger.info("Game over and scores calculated");
            return true;
        }
        logger.info("Game not over yet");
        return false;
    }
}
