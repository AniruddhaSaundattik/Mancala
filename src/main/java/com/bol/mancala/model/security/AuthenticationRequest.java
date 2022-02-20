package com.bol.mancala.model.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String userName;
    String password;
}

