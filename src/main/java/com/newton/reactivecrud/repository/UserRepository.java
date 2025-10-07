package com.newton.reactivecrud.repository;

import com.newton.reactivecrud.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for User entity.
 * Returns Mono (0-1 results) and Flux (0-N results) instead of blocking calls.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    // Custom query methods - these are non-blocking
    Mono<User> findByEmail(String email);

    Flux<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT * FROM users WHERE phone = :phone")
    Mono<User> findByPhoneNumber(String phone);
}
