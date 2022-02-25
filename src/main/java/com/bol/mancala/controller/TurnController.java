package com.bol.mancala.controller;

import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.model.TurnDTO;
import com.bol.mancala.service.TurnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/turn")
@Tag(name = "Player Turn API", description = "This API provides turn related functionality with transferring of stones")
public class TurnController {

    private static final Logger logger = LoggerFactory.getLogger(TurnController.class);

    @Autowired
    TurnService turnService;

    @Autowired
    Validator turnDtoValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(turnDtoValidator);
    }

    @Operation(summary = "API to execute player's turn")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "ok",
            content = @Content(mediaType = "application/json"))})
    @PostMapping
    public ResponseEntity<MancalaDTO> doTurn(@Valid @RequestBody TurnDTO turnDTO) throws MancalaException {
        logger.info("Playing turn");
        this.turnService.doTurn(turnDTO);
        return new ResponseEntity<>(turnDTO.getMancalaObj(), HttpStatus.OK);
    }

    @Operation(summary = "API to transfer stones")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "ok",
            content = @Content(mediaType = "application/json"))})
    @PostMapping("/transfer")
    public ResponseEntity<MancalaDTO> transferStones(@Valid @RequestBody TurnDTO turnDTO) throws MancalaException {
        logger.info("Transferring stones");
        this.turnService.transferStones(turnDTO);
        return new ResponseEntity<>(turnDTO.getMancalaObj(), HttpStatus.OK);
    }

    @Operation(summary = "API to transfer stones to big pit if last stone lands in empty pit")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "ok",
            content = @Content(mediaType = "application/json"))})
    @PostMapping("/transferEmpty")
    public ResponseEntity<MancalaDTO> transferToBigPitIfEmpty(@Valid @RequestBody TurnDTO turnDTO) throws MancalaException {
        logger.info("Transferring stones to big pit if last stone in player's empty pit");
        this.turnService.transferToBigPitIfEmpty(turnDTO);
        return new ResponseEntity<>(turnDTO.getMancalaObj(), HttpStatus.OK);
    }
}
