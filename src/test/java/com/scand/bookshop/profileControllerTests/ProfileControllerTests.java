package com.scand.bookshop.profileControllerTests;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.ProfileCredentialsDTO;
import com.scand.bookshop.dto.UserResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.util.auth.AuthSetUp;
import com.scand.bookshop.utility.ServerMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileControllerTests {
    private String jwtToken;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private RegistrationService registrationService;

    @BeforeAll
    private void createAdmin() {
        AuthSetUp.createAdmin(registrationService,"adminProfile","adminprof@mail.ru");
    }

    @BeforeEach
    void login() {
        jwtToken = AuthSetUp.login(port,"adminProfile","admin");
    }

    @Test
    public void getProfile_shouldReturnProfileInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/profile/me",
                HttpMethod.GET,
                httpEntity,
                UserResponseDTO.class
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isNotNull();
        assertThat(response.getBody().getEmail()).isNotNull();
        assertThat(response.getBody().getRegDate()).isNotNull();
    }

    @Test
    public void updateCredentials_shouldUpdateCredentials() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        String newEmail = "newEmail@mail.ru";
        ProfileCredentialsDTO newCredentials = new ProfileCredentialsDTO("admin", newEmail);
        HttpEntity<ProfileCredentialsDTO> requestEntity = new HttpEntity<ProfileCredentialsDTO>(newCredentials, headers);
        ResponseEntity<Void> response =
                restTemplate.postForEntity("http://localhost:" + port + "/profile/update",
                        requestEntity,
                        Void.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getEmail()).isEqualTo(newEmail);
    }

    @Test
    public void updateCredentials_shouldNotUpdateCredentialsWithNotValidData() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        String newEmail = "mail.ru";
        ProfileCredentialsDTO newCredentials = new ProfileCredentialsDTO("admin", newEmail);
        HttpEntity<ProfileCredentialsDTO> requestEntity = new HttpEntity<ProfileCredentialsDTO>(newCredentials, headers);
        assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/profile/update",
                    requestEntity,
                    Void.class);
        });
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getEmail()).isNotEqualTo(newEmail);
    }

    @Test
    public void uploadAvatar() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(new File("src/test/resources/files/bg.jpg"));
        body.add("avatar", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/profile/avatar/upload",
                requestEntity,
                Void.class);
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getAvatar()).isEqualTo("uploads/avatar/" + updatedUser.get().getUuid() + ".jpg");
    }

    @Test
    public void uploadAvatar_shouldNotUpdateWithBadFile() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(new File("src/test/resources/files/book1.pdf"));
        body.add("avatar", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/profile/avatar/upload",
                    requestEntity,
                    Void.class);
        });
        Optional<User> updatedUser = userRepository.findByLogin("adminProfile");
        assertThat(updatedUser.isPresent()).isTrue();
        assertThat(updatedUser.get().getAvatar()).isNotEqualTo("uploads/avatar/" + updatedUser.get().getUuid() + ".pdf");
    }
}

