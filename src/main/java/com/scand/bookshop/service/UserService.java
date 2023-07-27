package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    @Transactional
    public void updateCredentials(User user, String email, String password) {
        user = userRepository.getReferenceById(user.getId());
        String newHashPassword = PasswordEncryptor.encryptPassword(password);
        if (!Objects.equals(user.getPassword(), newHashPassword)) {
            user.setPassword(newHashPassword);
        }
        if (!Objects.equals(user.getEmail(), email)) {
            user.setEmail(email);
        }
    }
}
