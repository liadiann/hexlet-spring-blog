package io.hexlet.spring.controller;

import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> index() {
        var users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        var user = userRepository.findById(id);
        return ResponseEntity.of(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        if (user.getEmail().isEmpty()) {
            return null;
        }
        userRepository.save(user);
        return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User data) {
        var maybeUser = userRepository.findById(id);
        var status = HttpStatus.NOT_FOUND;
        if (maybeUser.isPresent()) {
            var user = maybeUser.get();
            user.setId(data.getId());
            user.setFirstName(data.getFirstName());
            user.setLastName(data.getLastName());
            user.setEmail(data.getEmail());
            userRepository.save(user);
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
