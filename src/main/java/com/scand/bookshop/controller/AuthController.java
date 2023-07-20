package com.scand.bookshop.controller;

import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.facade.AuthFacade;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.utility.ServerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping(value = "/register")
    public ServerMessage register(@NotNull @RequestBody @Valid UserRegistrationDTO userRegData) {
        return authFacade.register(userRegData);
    }

    @PostMapping(value = "/signin")
    public JwtResponse login(@NotNull @RequestBody @Valid UserLoginDTO userLoginData) {
        return authFacade.authenticateUser(userLoginData);
    }
}
