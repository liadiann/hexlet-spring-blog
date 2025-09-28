package io.hexlet.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "posts")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Заголовок не должен быть пустым")
    @Size(min = 10, message = "Заголовок должен быть не короче 10 символов")
    private String title;
    private String content;
    private Boolean published;
}
