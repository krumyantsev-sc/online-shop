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

    public User register(String username, String hashPassword, String email, LocalDateTime regDate, User.Role role) {
        User user = new User(null, java.util.UUID.randomUUID(),username,hashPassword,email,regDate, role, null);
        user = userRepository.save(user);
        return user;
    }
}
