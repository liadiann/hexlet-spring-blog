package io.hexlet.spring.controller.api;

import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.UserMapper;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UsersController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        return userMapper.toDTO(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody User user) {
        var savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable Long id, @RequestBody User data) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        if (data.getFirstName() != null) {
            user.setFirstName(data.getFirstName());
        }
        if (data.getLastName() != null) {
            user.setLastName(data.getLastName());
        }
        if (data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " not found"));
        userRepository.deleteById(id);
    }

}
