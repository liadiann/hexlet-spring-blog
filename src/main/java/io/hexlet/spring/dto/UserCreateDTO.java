package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCreateDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
}
