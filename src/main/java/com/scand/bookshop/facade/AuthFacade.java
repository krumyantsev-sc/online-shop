package com.scand.bookshop.facade;

import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.exception.UserAlreadyExistsException;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.utility.ServerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final UserService userService;
    private final RegistrationService registrationService;

    public ServerMessage register(UserRegistrationDTO userData) {
        if (userService.findUserByEmail(userData.getEmail()).isPresent()
                || userService.findUserByUsername(userData.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        registrationService.register(userData.getUsername(),
                PasswordEncryptor.encryptPassword(userData.getPassword()),
                userData.getEmail(),
                LocalDateTime.now());
        return new ServerMessage("success","user registered succesfully");
    }
}
