package com.scand.bookshop.facade;

import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.exception.UserAlreadyExistsException;
import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.service.LogInService;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.utility.ServerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final LogInService logInService;

    public ServerMessage register(UserRegistrationDTO userData) {
        if (userService.findUserByEmail(userData.getEmail()).isPresent()
                || userService.findUserByUsername(userData.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        registrationService.register(userData.getUsername(),
                PasswordEncryptor.encryptPassword(userData.getPassword()),
                userData.getEmail(),
                LocalDateTime.now(),
                User.Role.USER);
        return new ServerMessage("success", "user registered successfully");
    }

    public JwtResponse authenticateUser(UserLoginDTO loginRequest) {
        return logInService.logIn(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
