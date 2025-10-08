package io.hexlet.spring.service;

import io.hexlet.spring.dto.*;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.specification.PostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostMapper postMapper;
    private PostSpecification specBuilder;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       PostMapper postMapper, PostSpecification specBuilder) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.specBuilder = specBuilder;
    }

    public Page<PostDTO> getAll(PostParamsDTO params, Integer page) {
        var spec = specBuilder.build(params);
        var posts = postRepository.findAll(spec, PageRequest.of(page - 1, 10));
        return posts.map(postMapper::map);
    }

    public PostDTO findById(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        return postMapper.map(post);
    }

    public PostDTO create(PostCreateDTO data) {
        var user = userRepository.findById(data.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var post = postMapper.map(data);
        post.setAuthor(user);
        postRepository.save(post);
        return postMapper.map(post);
    }

    public PostDTO update(Long id, PostUpdateDTO data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postMapper.update(data, post);
        postRepository.save(post);
        return postMapper.map(post);
    }

    public PostDTO patch(Long id, PostPatchDTO data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postMapper.update(data, post);
        postRepository.save(post);
        return postMapper.map(post);
    }

    public void delete(Long id) {
        postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        postRepository.deleteById(id);
    }
}
