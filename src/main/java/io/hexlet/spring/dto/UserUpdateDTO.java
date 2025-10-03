package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
}
