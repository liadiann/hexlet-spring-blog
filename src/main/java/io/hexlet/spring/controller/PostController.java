package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok()
                .body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getTitle().equals(id))
                .findFirst();
        return ResponseEntity.of(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post) {
        if (post.getTitle() == null || post.getContent() == null
        || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return null;
        }
        posts.add(post);
        return ResponseEntity.status(201).body(post);
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
    public ResponseEntity<Void> destroy(@PathVariable String id) {
        var check = posts.removeIf(p -> p.getTitle().equals(id));
        if (check) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
