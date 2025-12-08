/**
 * REST API for user resources.
 *
 * Endpoints:
 * - GET /api/users
 * - GET /api/users/{id}
 * - POST /api/users
 * - PUT /api/users/{id}
 */
package com.example.usersdemo.web.api;

import java.util.List;
import java.util.UUID;

import com.example.usersdemo.user.User;
import com.example.usersdemo.user.UserNotFoundException;
import com.example.usersdemo.user.UserService;
import com.example.usersdemo.user.UserWriteModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /** List all users. */
    @GetMapping
    public List<User> listUsers() {
        return userService.findAll();
    }

    /** Get a single user by id or return 404. */
    @GetMapping("/{id}")
    public User findUser(@PathVariable UUID id) {
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    /** Create a new user. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserWriteModel input) {
        return userService.create(input);
    }

    /** Update an existing user. */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody UserWriteModel input) {
        try {
            return userService.update(id, input);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
