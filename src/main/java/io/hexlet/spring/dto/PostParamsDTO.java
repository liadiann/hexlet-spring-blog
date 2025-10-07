package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostParamsDTO {
    private Long authorId;
    private String titleCont;
    private LocalDateTime createdAtGt;
    private LocalDateTime createdAtLt;
    private Boolean publishedTrue;
}
