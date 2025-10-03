package io.hexlet.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateDTO {
    @Size(min = 1, max = 15)
    private String title;
    private String content;
}
