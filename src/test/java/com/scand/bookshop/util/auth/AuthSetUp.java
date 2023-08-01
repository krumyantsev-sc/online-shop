package com.scand.bookshop.util.auth;

import com.scand.bookshop.dto.UserLoginDTO;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.service.PasswordEncryptor;
import com.scand.bookshop.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class AuthSetUp {

    public static void createAdmin(RegistrationService registrationService, String username, String email) {
        registrationService.register(username,
                PasswordEncryptor.encryptPassword("admin"),
                email,
                LocalDateTime.now(),
                User.Role.ADMIN);
    }

    public static String login(int port, String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserLoginDTO userData = new UserLoginDTO(login, password);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserLoginDTO> entity = new HttpEntity<UserLoginDTO>(userData, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:" + port + "/auth/signin", entity, Map.class);
        return Objects.requireNonNull(response.getBody()).get("accessToken").toString();
    }
}
