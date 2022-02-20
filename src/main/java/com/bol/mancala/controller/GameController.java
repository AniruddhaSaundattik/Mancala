package com.bol.mancala.controller;

import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.GameStartDTO;
import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Tag(name = "Game API", description = "This API provides endpoints for starting & ending game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    GameService gameService;

    @Autowired
    private Validator[] validators;

    @InitBinder
    public void setUpValidators(WebDataBinder webDataBinder) {
        for (Validator validator : validators) {
            if (validator.supports(webDataBinder.getTarget().getClass())
                    && !validator.getClass().getName().contains("org.springframework"))
                webDataBinder.addValidators(validator);
        }
    }

    @Operation(summary = "API to start the game")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Game created",
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request")})
    @PostMapping(value = "/start", produces = "application/json")
    public ResponseEntity<MancalaDTO> startGame(@Valid @RequestBody GameStartDTO gameStartDTO) throws MancalaException {
        logger.info("Starting game");
        return new ResponseEntity(gameService.startGame(gameStartDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "API to execute game over")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Game Over",
            content = @Content(mediaType = "application/json"))})
    @PostMapping("/gameOver")
    public ResponseEntity<MancalaDTO> gameOverCalculation(@Valid @RequestBody MancalaDTO mancalaObj) throws MancalaException {
        logger.info("Calculating final score and announcing winner");
        this.gameService.executeGameOver(mancalaObj);
        return new ResponseEntity(mancalaObj, HttpStatus.OK);
    }

}
