package com.disl.boilerplate.repository;


import com.disl.boilerplate.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(long id);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserIdAndExpiryDateIsBefore(Long userId, Instant currentTime);
}
