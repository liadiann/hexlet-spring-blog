package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean published;
    private Long authorId;
    private LocalDateTime createdAt;
}
