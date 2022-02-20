package com.bol.mancala.service;

import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.model.TurnDTO;
import com.bol.mancala.service.impl.GameServiceImpl;
import com.bol.mancala.service.impl.TurnServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TurnServiceTest {

    @InjectMocks
    TurnServiceImpl turnService;

    @Mock
    GameServiceImpl gameService;

    @Test
    void doTurn1() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        TurnDTO turn = new TurnDTO("Play1", 1, mancalaObj);
        this.turnService.doTurn(turn);
        assertArrayEquals(new int[]{5, 0, 6, 6, 6, 6}, mancalaObj.getPlayer1().getSmallPitsCount());
        assertEquals(1, mancalaObj.getPlayer1().getBigPitCount());
        assertTrue(mancalaObj.isRepeatTurn());
    }

    @Test
    void doTurn2() {
        Mockito.when(gameService.checkGameOver(Mockito.any(MancalaDTO.class))).thenReturn(true);
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        mancalaObj.getPlayer1().setSmallPitsCount(new int[]{0, 0, 0, 0, 0, 8});
        mancalaObj.getPlayer2().setSmallPitsCount(new int[]{1, 2, 3, 4, 5, 6});
        TurnDTO turn1 = new TurnDTO("Play1", 5, mancalaObj);
        this.turnService.doTurn(turn1);
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0}, mancalaObj.getPlayer1().getSmallPitsCount());
        assertEquals(9, mancalaObj.getPlayer1().getBigPitCount());
        assertFalse(mancalaObj.isRepeatTurn());
        assertTrue(mancalaObj.isGameOver());
    }

    @Test
    void transferStones() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        TurnDTO turn = new TurnDTO("Play1", 1, mancalaObj);
        this.turnService.transferStones(turn);
        assertArrayEquals(new int[]{5, 0, 6, 6, 6, 6}, mancalaObj.getPlayer1().getSmallPitsCount());
        assertEquals(1, mancalaObj.getPlayer1().getBigPitCount());
        assertTrue(mancalaObj.isRepeatTurn());
        TurnDTO turn2 = new TurnDTO("Play2", 1, mancalaObj);
        this.turnService.transferStones(turn2);
        assertArrayEquals(new int[]{5, 0, 6, 6, 6, 6}, mancalaObj.getPlayer2().getSmallPitsCount());
        assertEquals(1, mancalaObj.getPlayer2().getBigPitCount());
    }

    @Test
    void transferToBigPitIfEmpty() {
        MancalaDTO mancalaObj = new MancalaDTO(6, 5, "Play1", "Play2");
        mancalaObj.setLastStoneInEmptyPit(true);
        mancalaObj.setLastPitNumber(0);
        TurnDTO turn = new TurnDTO("Play1", 1, mancalaObj);
        this.turnService.transferToBigPitIfEmpty(turn);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5, 0}, mancalaObj.getPlayer2().getSmallPitsCount());
        assertArrayEquals(new int[]{0, 5, 5, 5, 5, 5}, mancalaObj.getPlayer1().getSmallPitsCount());
        assertEquals(6, mancalaObj.getPlayer1().getBigPitCount());
    }
}