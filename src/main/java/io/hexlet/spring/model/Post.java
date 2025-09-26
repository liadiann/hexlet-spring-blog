package io.hexlet.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Post {
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
