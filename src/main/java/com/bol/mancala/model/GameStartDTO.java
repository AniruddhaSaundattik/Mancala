package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStartDTO {

    @Min(1)
    int smallPits;
    @Min(1)
    int smallPitStonesCount;
    String player1;
    String player2;
}
