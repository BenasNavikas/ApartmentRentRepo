package com.ar.apartmentrent.repository;

import com.ar.apartmentrent.model.RefreshToken;
import com.ar.apartmentrent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser_Username(String username);

}
