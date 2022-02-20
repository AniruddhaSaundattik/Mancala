package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MancalaDTO {
    private boolean gameOver;
    private boolean lastStoneInEmptyPit;
    private boolean repeatTurn;
    private int lastPitNumber;
    private PlayerDTO player1;
    private PlayerDTO player2;
    private String winner;
    
    public MancalaDTO (int smallPitCount, int smallPitStonesCount, String name1, String name2){
        lastPitNumber = -1;
        player1 = new PlayerDTO(name1, smallPitCount, smallPitStonesCount);
        player2 = new PlayerDTO(name2, smallPitCount, smallPitStonesCount);
    }

    public MancalaDTO (int smallPitCount, int smallPitStonesCount){
        this(smallPitCount, smallPitStonesCount, "Player1", "Player2");
    }

}
