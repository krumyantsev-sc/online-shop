package com.scand.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String username;
    private String email;
    private String regDate;
}
