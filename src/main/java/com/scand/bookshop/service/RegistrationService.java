package com.scand.bookshop.service;

import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private static final org.apache.logging.log4j.Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(RegistrationService.class);
    public User register(String username, String hashPassword, String email, LocalDateTime regDate, User.Role role) {
        logger.info("Starting registration process for user: " + username);
        User newUser = userRepository.save(new User(
                null,
                java.util.UUID.randomUUID(),
                username,
                hashPassword,
                email,
                regDate,
                role,
                null));
        logger.info("User: " + username + " registered successfully.");
        return newUser;
    }
}
