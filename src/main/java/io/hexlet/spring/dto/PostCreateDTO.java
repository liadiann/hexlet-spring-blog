package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateDTO {
    @NotBlank
    @Size(min = 3, max = 15)
    private String title;
    @NotBlank
    private String content;
    private Boolean published;
}
