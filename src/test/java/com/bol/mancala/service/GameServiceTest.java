package com.bol.mancala.service;

import com.bol.mancala.model.GameStartDTO;
import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void startGame() {
        GameStartDTO mockStart = new GameStartDTO(5, 5, "Play1", "Play2");
        MancalaDTO mancalaObj = this.gameService.startGame(mockStart);
        assertEquals(mancalaObj.getPlayer1().getSmallPitsCount().length, 5);
        assertEquals(mancalaObj.getPlayer1().getSmallPitsCount()[1], 5);
        assertEquals(mancalaObj.getPlayer1().getName(), "Play1");
        assertEquals(mancalaObj.getPlayer2().getName(), "Play2");
    }

    @Test
    void executeGameOver() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        mancalaObj.setGameOver(true);
        mancalaObj.getPlayer1().setSmallPitsCount(new int[]{1,2,3,0,0,0});
        mancalaObj.getPlayer1().setBigPitCount(5);
        mancalaObj.getPlayer2().setSmallPitsCount(new int[7]);
        mancalaObj.getPlayer2().setBigPitCount(13);
        this.gameService.executeGameOver(mancalaObj);
        assertEquals("Play2", mancalaObj.getWinner());
        assertEquals(11, mancalaObj.getPlayer1().getScore());
        assertEquals(13, mancalaObj.getPlayer2().getScore());
    }

    @Test
    void checkDraw() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        mancalaObj.setGameOver(true);
        mancalaObj.getPlayer1().setSmallPitsCount(new int[]{0,1,2,3,0,0,0});
        mancalaObj.getPlayer1().setBigPitCount(5);
        mancalaObj.getPlayer2().setSmallPitsCount(new int[7]);
        mancalaObj.getPlayer2().setBigPitCount(11);
        this.gameService.executeGameOver(mancalaObj);
        assertEquals("Draw", mancalaObj.getWinner());
        assertEquals(11, mancalaObj.getPlayer1().getScore());
    }

    @Test
    void executeGameOverPlayer1() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        mancalaObj.setGameOver(true);
        mancalaObj.getPlayer1().setSmallPitsCount(new int[]{0,1,2,3,0,0,0});
        mancalaObj.getPlayer1().setBigPitCount(5);
        mancalaObj.getPlayer2().setSmallPitsCount(new int[7]);
        mancalaObj.getPlayer2().setBigPitCount(8);
        this.gameService.executeGameOver(mancalaObj);
        assertEquals("Play1", mancalaObj.getWinner());
        assertEquals(11, mancalaObj.getPlayer1().getScore());
        assertEquals(8, mancalaObj.getPlayer2().getScore());
    }
}