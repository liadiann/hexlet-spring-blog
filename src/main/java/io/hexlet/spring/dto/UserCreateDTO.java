package io.hexlet.spring.dto;

import lombok.Getter;

@Getter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
}
