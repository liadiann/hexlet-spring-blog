package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostDTO toDTO(Post post) {
        var dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublished(post.getPublished());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    public Post toEntity(PostCreateDTO dto) {
        var post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPublished(dto.getPublished());
        return post;
    }
}
