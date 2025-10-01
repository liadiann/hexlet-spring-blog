package io.hexlet.spring.controller.api;

import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
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
        return postRepository.findByPublishedTrue(pageRequest).map(postMapper::toDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        return postMapper.toDTO(post);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@RequestBody Post post) {
        var savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDTO(savedPost));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO update(@PathVariable Long id, @RequestBody Post data) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        if (data.getTitle() != null) {
            post.setTitle(data.getTitle());
        }
        if (data.getContent() != null) {
            post.setContent(data.getContent());
        }
        if (data.getPublished() != null) {
            post.setPublished(data.getPublished());
        }
        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postRepository.deleteById(id);
    }
}
