package com.scand.bookshop.facade;

import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.FileService;
import com.scand.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


@Component
@RequiredArgsConstructor
public class ProfileFacade {
    private final UserService userService;
    private final FileService fileService;
    public UserResponseDTO getUserProfile(UserDetailsImpl userPrincipal) {
        return DTOConverter.toUserDTO(userService.findUserById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    public void updateCredentials(UserDetailsImpl userPrincipal, ProfileCredentialsDTO updatedCredentials) {
        User user = userService.getUserById(userPrincipal.getId());
        userService.updateCredentials(user, updatedCredentials.getEmail(), updatedCredentials.getPassword());
    }

    public Resource getAvatar(UserDetailsImpl userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        return userService.getAvatar(user);
    }

    final String PNG_EXTENSION = "png";
    final String JPEG_EXTENSION = "jpg";
    public void uploadAvatar(UserDetailsImpl userPrincipal, MultipartFile file) {
        User user = userService.getUserById(userPrincipal.getId());
        final List<String> allowedExtensions = List.of(PNG_EXTENSION, JPEG_EXTENSION);
        String fileExtension = fileService.getExtension(file);
        String extension = allowedExtensions.stream()
                .filter(allowedExtension -> allowedExtension.equals(fileExtension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Wrong extension"));
        try {
            userService.uploadAvatar(user, file.getBytes(), extension);
        } catch (IOException e) {
            throw new RuntimeException("Error reading bytes");
        }
    }
}
