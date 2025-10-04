package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateDTO {
    @NotBlank
    @Size(min = 1, max = 15)
    private String title;
    @NotBlank
    private String content;
}
