package com.scand.bookshop.service;

import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.security.jwt.JwtUtils;
import com.scand.bookshop.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogInService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public JwtResponse logIn(String username, String password) {
        log.info("Attempting to authenticate user: " + username);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return generateToken(authentication);
    }

    private JwtResponse generateToken(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Generated JWT for user: " + userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }
}
