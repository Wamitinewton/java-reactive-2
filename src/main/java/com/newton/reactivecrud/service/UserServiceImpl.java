package com.newton.reactivecrud.service;

import com.newton.reactivecrud.model.User;
import com.newton.reactivecrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user.
     * Mono represents an asynchronous computation that returns 0 or 1 result.
     */
    @Override
    public Mono<User> createUser(User user) {
        log.info("Creating user {}", user);
        return userRepository.save(user)
                .doOnSuccess(u -> log.info("Created user {}", u))
                .doOnError(u -> log.error("Error creating user", u));
    }

    /**
     * Retrieves user by ID.
     * switchIfEmpty provides a fallback if user is not found.
     */
    @Override
    public Mono<User> getUserById(Long id) {
        log.info("Retrieving user by id {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User with id " + id + " not found")
                ));
    }

    /**
     * Retrieves all users.
     * Flux represents an asynchronous stream of 0 to N elements.
     */
    @Override
    public Flux<User> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll()
                .doOnComplete(() -> log.info("All users retrieved"));
    }

    /**
     * Updates an existing user.
     * flatMap chains asynchronous operations - waits for findById,
     * then executes the save operation.
     */
    @Override
    public Mono<User> updateUser(Long id, User user) {
        log.info("Updating user {}", user);
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setEmail(user.getEmail());
                    existingUser.setName(user.getName());
                    existingUser.setPhone(user.getPhone());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User with id " + id + " not found")
                ))
                .doOnSuccess(u -> log.info("Updated user {}", u));

    }

    /**
     * Deletes a user by ID.
     * Mono<Void> represents an asynchronous operation with no return value.
     */
    @Override
    public Mono<Void> deleteUser(Long id) {
        log.info("Deleting user {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("User not found with id: " + id)
                ))
                .flatMap(userRepository::delete)
                .doOnSuccess(u -> log.info("Deleted user {}", u));
    }

    /**
     * Searches users by name (partial match).
     * Demonstrates custom query methods with Flux.
     */
    @Override
    public Flux<User> searchUsersByName(String name) {
        log.info("Searching users by name: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }
}
