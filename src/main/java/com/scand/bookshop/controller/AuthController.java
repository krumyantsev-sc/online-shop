package com.scand.bookshop.controller;

import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.facade.AuthFacade;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.utility.ServerMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping(value = "/register")
    public ServerMessage register(@NotNull @RequestBody @Valid UserRegistrationDTO userRegData) {
        System.out.println(userRegData.toString());
        return authFacade.register(userRegData);
    }

    @PostMapping(value = "/signin")
    public JwtResponse login(@NotNull @Valid @RequestBody UserLoginDTO userLoginData) {
        return authFacade.authenticateUser(userLoginData);
    }
}
