package com.ar.apartmentrent.services;


import com.ar.apartmentrent.config.AuthenticationRequest;
import com.ar.apartmentrent.config.AuthenticationResponse;
import com.ar.apartmentrent.config.RegisterRequest;
import com.ar.apartmentrent.model.Role;
import com.ar.apartmentrent.model.User;
import com.ar.apartmentrent.model.dto.LessorDTO;
import com.ar.apartmentrent.repository.UserRepository;
import com.ar.apartmentrent.services.impl.LesseeServiceImpl;
import com.ar.apartmentrent.services.impl.LessorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final LessorServiceImpl lessorService;
    private final LesseeServiceImpl lesseeService;
    private final UserRepository userRepository;

    public Role pickRole(String role) {
        switch (role) {
            case "LESSOR":
                return Role.LESSOR;
            case "LESSEE":
                return Role.LESSEE;
            case "USER":
                return Role.USER;
            case "ADMIN":
                return Role.ADMIN;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public void register(RegisterRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(pickRole(request.getRole()))
                .build();
        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }


}
