package com.scand.bookshop.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileCredentialsDTO {
    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank @Email
    private String email;
}
