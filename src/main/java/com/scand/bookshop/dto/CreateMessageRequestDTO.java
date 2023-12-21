package com.scand.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequestDTO {

  @NotBlank
  private String message;
  @NotBlank
  private String uuid;
}
