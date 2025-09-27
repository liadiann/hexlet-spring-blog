package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<List<Post>> index() {
        var posts = postRepository.findAll();
        return ResponseEntity.ok()
                .body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        var post = postRepository.findById(id);
        return ResponseEntity.of(post);
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        if (post.getTitle() == null || post.getContent() == null
        || post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            return null;
        }
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post data) {
        if (data.getTitle() == null || data.getContent() == null
                || data.getTitle().isEmpty() || data.getContent().isEmpty()) {
            return null;
        }
        var postBefore = postRepository.findById(id);
        var status = HttpStatus.NOT_FOUND;
        if (postBefore.isPresent()) {
            var postAfter = postBefore.get();
            postAfter.setContent(data.getContent());
            postAfter.setTitle(data.getTitle());
            postAfter.setPublished(data.getPublished());
            postRepository.save(postAfter);
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
