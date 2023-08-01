package com.scand.bookshop.logInServiceTests;

import com.scand.bookshop.security.jwt.JwtResponse;
import com.scand.bookshop.service.LogInService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.util.auth.AuthSetUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogInServiceTests {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogInService logInService;

    @BeforeAll
    private void registerUser() {
        AuthSetUp.createAdmin(registrationService,"admin","admin@mail.ru");
    }

    @Test
    public void logIn_shouldReturnToken() {
        JwtResponse jwtResponse = logInService.logIn("admin", "admin");
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.getAccessToken()).isNotNull();
        assertThat(jwtResponse.getUsername()).isEqualTo("admin");
    }

    @Test
    public void logIn_shouldNotReturnTokenWithInvalidCredentials() {
        JwtResponse jwtResponse = null;
        try {
            jwtResponse = logInService.logIn("admin", "123123");
        } catch (BadCredentialsException e) {
            // expected
        }
        assertThat(jwtResponse).isNull();
    }
}
