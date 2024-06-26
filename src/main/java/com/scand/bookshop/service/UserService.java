package com.scand.bookshop.service;

import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUuid(UUID uuid) {
        log.info("Fetching user by UUID: " + uuid);
        return userRepository.findByUuid(uuid);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    public User getUserById(Long id) {
        log.info("Retrieving user by ID: " + id);
        return findUserById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID " + id + " not found.");
                    return new NoSuchElementException(messageSource.getMessage("user_not_found", null, request.getLocale()));
                });
    }

    @Transactional
    public void updateCredentials(User user, String email, String password) {
        log.info("Updating credentials for user ID: " + user.getId());
        user = userRepository.getReferenceById(user.getId());
        String newHashPassword = PasswordEncryptor.encryptPassword(password);
        if (!Objects.equals(user.getPassword(), newHashPassword)) {
            user.setPassword(newHashPassword);
        }
        user.setEmail(email);
        log.info("Credentials updated for user ID: " + user.getId());
    }

    public Resource getAvatar(User user) {
        log.info("Fetching avatar for user ID: " + user.getId());
        Path file = Paths.get(user.getAvatar());
        return fileService.getImageResource(file);
    }

    @Transactional
    public void uploadAvatar(User user, byte[] content, String extension) {
        log.info("Uploading avatar for user ID: " + user.getId());
        user = userRepository.getReferenceById(user.getId());
        String filePath = String.format("uploads/avatar/%s.%s", user.getUuid(), extension);
        user.setAvatar(filePath);
        Path file = Paths.get(filePath);
        fileService.deleteIfExists(file);
        fileService.writeFile(Paths.get(filePath), content);
        log.info("Avatar uploaded successfully for user ID: " + user.getId());
    }
}
