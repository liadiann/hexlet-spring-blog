package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public List<Post> index() {
        return posts;
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getTitle().equals(id))
                .findFirst();
        return post;
    }

    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        if (post.getTitle() == null || post.getContent() == null
        || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return null;
        }
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    public Post update(@PathVariable String id, @RequestBody Post data) {
        if (data.getTitle() == null || data.getContent() == null
                || data.getTitle().isEmpty() || data.getContent().isEmpty()) {
            return null;
        }
        var postBefore = posts.stream()
                .filter(p -> p.getTitle().equals(id))
                .findFirst();
        if (postBefore.isPresent()) {
            var postAfter = postBefore.get();
            postAfter.setAuthor(data.getAuthor());
            postAfter.setContent(data.getContent());
            postAfter.setTitle(data.getTitle());
            postAfter.setCreatedAt(data.getCreatedAt());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getTitle().equals(id));
    }
}
