package com.ar.apartmentrent.services;

import com.ar.apartmentrent.model.RefreshToken;
import com.ar.apartmentrent.model.User;
import com.ar.apartmentrent.repository.RefreshTokenRepository;
import com.ar.apartmentrent.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;


//    RefreshToken refreshToken = RefreshToken.builder()
//            .user(userRepository.findByUsername(username).get())
//            .token(UUID.randomUUID().toString())
//            .expiryDate(Instant.now().plusMillis(600000))//10
//            .build();
//        return refreshTokenRepository.save(refreshToken);

    public RefreshToken createRefreshToken(String username){

//        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser_Username(username);
//        RefreshToken refreshToken = optionalRefreshToken.orElseGet(RefreshToken::new);
//        User user = userRepository.findByUsername(username).get();
//
//        RefreshToken.builder()
//                .user(user)
//                .token(UUID.randomUUID().toString())
//                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes expiration
//                .build();
//        return refreshTokenRepository.save(refreshToken);
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser_Username(username);
        RefreshToken refreshToken = optionalRefreshToken.orElseGet(RefreshToken::new);
        User user = userRepository.findByUsername(username).get();

        String token = UUID.randomUUID().toString();

        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(6000000));

        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. PLease make a new signin request");
        }
        return token;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
