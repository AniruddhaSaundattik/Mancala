package com.bol.mancala.controller;

import com.bol.mancala.model.GameStartDTO;
import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.security.JwtRequestFilter;
import com.bol.mancala.service.GameService;
import com.bol.mancala.service.security.JwtService;
import com.bol.mancala.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(controllers = GameController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)})
class GameControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GameService gameService;

    @MockBean
    @Qualifier("gameStartDtoValidator")
    Validator gameStartDtoValidator;

    @MockBean
    @Qualifier("mancalaDtoValidator")
    Validator mancalaDtoValidator;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtRequestFilter filter;

    @MockBean
    private JwtService jwtService;

    JacksonTester<GameStartDTO> jsonGameStart;

    JacksonTester<MancalaDTO> jsonMancalaDto;

    String jwtToken;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        Mockito.when(gameStartDtoValidator.supports(any())).thenReturn(true);
        Mockito.doNothing().when(gameStartDtoValidator).validate(any(), any(Errors.class));
        Mockito.when(mancalaDtoValidator.supports(any())).thenReturn(true);
        Mockito.doNothing().when(mancalaDtoValidator).validate(any(), any(Errors.class));
        UserDetails userDetails = new User("test", "test", new ArrayList<>());
        jwtToken = jwtUtil.generateToken(userDetails);
        Mockito.when(jwtService.loadUserByUsername(eq("test"))).thenReturn(userDetails);
    }

    @Test
    void startGame() throws Exception {
        Mockito.when(gameService.startGame(any(GameStartDTO.class))).thenReturn(new MancalaDTO(5, 5));

        String requestBody = jsonGameStart.write(new GameStartDTO(5, 5, "Player1", "Player2")).getJson();
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/start")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer " + jwtToken))
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        assertEquals(jsonMancalaDto.write(new MancalaDTO(5,
                        5, "Player1", "Player2")).getJson(),
                response.getContentAsString());
    }

    @Test
    void gameOverCalculation() throws Exception {
        Mockito.doNothing().when(this.gameService).executeGameOver(any(MancalaDTO.class));
        String requestBody = jsonMancalaDto.write(new MancalaDTO(4, 4, "", "")).getJson();
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/gameOver")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer " + jwtToken))
                .andReturn().getResponse();
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getContentAsString(), jsonMancalaDto.write(new MancalaDTO(4, 4, "", "")).getJson());
    }
}