package io.hexlet.spring.controller;

import io.hexlet.spring.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<User>> index(@RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok().body(users.stream().limit(limit).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        var user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        if (user.getEmail().isEmpty()) {
            return null;
        }
        users.add(user);
        return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User data) {
        var maybeUser = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        var status = HttpStatus.NOT_FOUND;
        if (maybeUser.isPresent()) {
            var user = maybeUser.get();
            user.setId(data.getId());
            user.setName(data.getName());
            user.setEmail(data.getEmail());
            status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        var check = users.removeIf(u -> u.getId().equals(id));
        if (check) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
