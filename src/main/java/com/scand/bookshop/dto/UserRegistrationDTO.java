package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    @NotBlank
    @Size(min = 5, max = 20)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    private String username;

    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank @Email
    private String email;
}
