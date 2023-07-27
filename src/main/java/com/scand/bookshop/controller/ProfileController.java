package com.scand.bookshop.controller;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.facade.BookFacade;
import com.scand.bookshop.facade.ProfileFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileFacade profileFacade;

    @GetMapping("/me")
    public UserResponseDTO getCurrentUserProfile(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
        return profileFacade.getUserProfile(userPrincipal);
    }

    @PostMapping("/update")
    public void updateCredentials(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                             @RequestBody @Valid ProfileCredentialsDTO updatedCredentials) {
        profileFacade.updateCredentials(userPrincipal, updatedCredentials);
    }
}
