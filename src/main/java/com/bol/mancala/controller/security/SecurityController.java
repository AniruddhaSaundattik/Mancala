package com.bol.mancala.controller.security;

import com.bol.mancala.exception.MancalaException;
import com.bol.mancala.model.security.AuthenticationRequest;
import com.bol.mancala.model.security.AuthenticationResponse;
import com.bol.mancala.service.security.JwtService;
import com.bol.mancala.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @ApiOperation(
            notes = "JWT Token",
            value = "Generate a valid JWT Token"
    )
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest)
            throws BadCredentialsException, MancalaException {
        logger.info("Generating JWT token");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName()
                        , authenticationRequest.getPassword())
        );
        UserDetails userDetails = jwtService.loadUserByUsername(authenticationRequest.getUserName());
        return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails)));
    }
}
