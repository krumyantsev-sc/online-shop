package com.scand.bookshop.facade;

import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
@RequiredArgsConstructor
public class ProfileFacade {
    private final UserService userService;

    public UserResponseDTO getUserProfile(UserDetailsImpl userPrincipal) {
        return DTOConverter.toUserDTO(userService.findUserById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    public void updateCredentials(UserDetailsImpl userPrincipal, ProfileCredentialsDTO updatedCredentials) {
        User user = userService.findUserById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userService.updateCredentials(user, updatedCredentials.getEmail(), updatedCredentials.getPassword());
    }
}
