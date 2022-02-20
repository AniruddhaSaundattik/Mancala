package com.bol.mancala.controller;

import com.bol.mancala.model.MancalaDTO;
import com.bol.mancala.model.TurnDTO;
import com.bol.mancala.security.JwtRequestFilter;
import com.bol.mancala.service.security.JwtService;
import com.bol.mancala.service.TurnService;
import com.bol.mancala.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TurnController.class, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)})
class TurnControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TurnService turnService;

    @MockBean
    @Qualifier("turnDtoValidator")
    Validator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtRequestFilter filter;

    @MockBean
    private JwtService jwtService;

    JacksonTester<TurnDTO> jsonTurnDto;

    JacksonTester<MancalaDTO> jsonMancalaDto;

    String jwtToken;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        Mockito.when(validator.supports(any())).thenReturn(true);
        Mockito.doNothing().when(validator).validate(any(), any(Errors.class));
        UserDetails userDetails = new User("test", "test", new ArrayList<>());
        jwtToken = jwtUtil.generateToken(userDetails);
        Mockito.when(jwtService.loadUserByUsername(eq("test"))).thenReturn(userDetails);
    }

    @Test
    void doTurn() throws Exception {
        Mockito.doNothing().when(this.turnService)
                .doTurn(Mockito.any(TurnDTO.class));
        String request = jsonTurnDto.write(new TurnDTO("Player1", 2,
                new MancalaDTO(4, 4))).getJson();
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/turn")
                    .contentType(MediaType.APPLICATION_JSON).content(request)
                    .header("Authorization", "Bearer " + jwtToken))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals(jsonMancalaDto.write(new MancalaDTO(4,
                        4, "Player1", "Player2")).getJson(),
                response.getContentAsString());
    }

    @Test
    void transferStones() throws Exception {
        Mockito.doNothing().when(this.turnService)
                .transferStones(Mockito.any(TurnDTO.class));
        String request = jsonTurnDto.write(new TurnDTO("Player1", 2,
                new MancalaDTO(4, 4))).getJson();
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/turn/transfer")
                    .contentType(MediaType.APPLICATION_JSON).content(request)
                    .header("Authorization", "Bearer " + jwtToken))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals(jsonMancalaDto.write(new MancalaDTO(4,
                        4, "Player1", "Player2")).getJson(),
                response.getContentAsString());
    }

    @Test
    void transferToBigPitIfEmpty() throws Exception {
        Mockito.doNothing().when(this.turnService)
                .transferToBigPitIfEmpty(Mockito.any(TurnDTO.class));
        String request = jsonTurnDto.write(new TurnDTO("Player1", 2,
                new MancalaDTO(4, 4))).getJson();
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/turn/transferEmpty")
                    .contentType(MediaType.APPLICATION_JSON).content(request)
                    .header("Authorization", "Bearer " + jwtToken))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals(jsonMancalaDto.write(new MancalaDTO(4,
                        4, "Player1", "Player2")).getJson(),
                response.getContentAsString());
    }

}