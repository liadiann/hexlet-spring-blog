package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDTO {
    @NotBlank
    @Size(min = 1, max = 15)
    private String title;
    @NotBlank
    private String content;
    private Boolean published;
    private Long authorId;
}
