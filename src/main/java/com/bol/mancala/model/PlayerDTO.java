package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class PlayerDTO {
    @NotNull
    private String name;
    private int score;
    private int bigPitCount;
    private int[] smallPitsCount;

    public PlayerDTO (String name, int smallPits, int smallPitStonesCount) {
        this.name = name;
        this.smallPitsCount = new int[smallPits];
        Arrays.fill(smallPitsCount, smallPitStonesCount);
    }
}
