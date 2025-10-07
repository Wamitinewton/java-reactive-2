package com.newton.reactivecrud.controller;

import com.newton.reactivecrud.model.User;
import com.newton.reactivecrud.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller handling HTTP requests reactively.
 * Methods return Mono/Flux instead of blocking responses.
 * This allows the server to handle thousands of concurrent requests
 * with a small thread pool (typically 2x CPU cores).
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     * @Valid triggers validation defined in User entity.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Retrieves a user by ID.
     * Mono automatically handles 404 when empty.
     */
    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Retrieves all users as a stream.
     * MediaType.TEXT_EVENT_STREAM enables Server-Sent Events (SSE)
     * for real-time streaming to the client.
     */
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retrieves all users as JSON array (standard REST response).
     */
    @GetMapping("/list")
    public Flux<User> getAllUsersList() {
        return userService.getAllUsers();
    }

    /**
     * Updates an existing user.
     */
    @PutMapping("/{id}")
    public Mono<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /**
     * Deletes a user by ID.
     * Mono<Void> returns empty response with appropriate status code.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * Searches users by name (query parameter).
     * Example: /api/users/search?name=john
     */
    @GetMapping("/search")
    public Flux<User> searchUsers(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }
}
