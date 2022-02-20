package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TurnDTO {
    @NotNull
    private String player;
    @NotNull
    private Integer pitClicked;
    @NotNull
    private MancalaDTO mancalaObj;
}
