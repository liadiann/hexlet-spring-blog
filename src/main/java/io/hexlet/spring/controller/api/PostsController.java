package io.hexlet.spring.controller.api;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostsController(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDTO> index(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer limit) {
        var sort = Sort.by(Sort.Order.desc("createdAt"));
        var pageRequest = PageRequest.of(page - 1, limit, sort);
        return postRepository.findByPublishedTrue(pageRequest).map(postMapper::map);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        return postMapper.map(post);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO postData) {
        var post = postMapper.map(postData);
        var savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.map(savedPost));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO postData) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postMapper.update(postData, post);
        postRepository.save(post);
        return postMapper.map(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postRepository.deleteById(id);
    }
}
