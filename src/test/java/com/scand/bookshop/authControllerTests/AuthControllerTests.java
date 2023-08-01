package com.scand.bookshop.authControllerTests;

import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.dto.UserRegistrationDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.utility.ServerMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void register_shouldRegisterUser() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserRegistrationDTO regDto = new UserRegistrationDTO("usertest",
                "usertest",
                "usertest@test.com");
        HttpEntity<UserRegistrationDTO> requestEntity = new HttpEntity<>(regDto, headers);
        ResponseEntity<ServerMessage> response =
                restTemplate.postForEntity("http://localhost:" + port + "/auth/register",
                        requestEntity,
                        ServerMessage.class);
        Optional<User> user = userRepository.findByLogin("usertest");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getLogin()).isEqualTo("usertest");
    }

    @Test
    public void register_shouldNotRegisterWithInvalidData() {
        RestTemplate restTemplate = new RestTemplate();
        UserRegistrationDTO regDto = new UserRegistrationDTO("usertest123",
                "s",
                "usertest123.com");
        HttpEntity<UserRegistrationDTO> requestEntity = new HttpEntity<UserRegistrationDTO>(regDto);
        assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/auth/register", requestEntity, ServerMessage.class);
        });
        Optional<User> user = userRepository.findByLogin("usertest123");
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    public void login_shouldLogInUser() {
        RestTemplate restTemplate = new RestTemplate();
        registrationService.register("usertestlogin",
                PasswordEncryptor.encryptPassword("usertestlogin"),
                "userlogin@user.com",
                LocalDateTime.now(),
                User.Role.USER);
        UserLoginDTO userLoginDTO = new UserLoginDTO("usertestlogin","usertestlogin");
        HttpEntity<UserLoginDTO> requestEntity = new HttpEntity<UserLoginDTO>(userLoginDTO);
        ResponseEntity<ServerMessage> response =
                restTemplate.postForEntity("http://localhost:" + port + "/auth/signin",
                        requestEntity,
                        ServerMessage.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void login_shouldNotLogInWithWrongCredentials() {
        RestTemplate restTemplate = new RestTemplate();
        registrationService.register("usertes1t",
                PasswordEncryptor.encryptPassword("usertes1t"),
                "user1@user.com",
                LocalDateTime.now(),
                User.Role.USER);
        UserLoginDTO userLoginDTO = new UserLoginDTO("usertes1t","123123");
        HttpEntity<UserLoginDTO> requestEntity = new HttpEntity<UserLoginDTO>(userLoginDTO);
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/auth/signin", requestEntity, ServerMessage.class);
        });
    }
}
