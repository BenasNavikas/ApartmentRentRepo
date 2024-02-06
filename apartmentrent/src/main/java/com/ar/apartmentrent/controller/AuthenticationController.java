package com.ar.apartmentrent.controller;

import com.ar.apartmentrent.config.AuthenticationRequest;
import com.ar.apartmentrent.config.AuthenticationResponse;
import com.ar.apartmentrent.config.RefreshTokenRequest;
import com.ar.apartmentrent.config.RegisterRequest;
import com.ar.apartmentrent.exception.InvalidTokenException;
import com.ar.apartmentrent.exception.LogoutException;
import com.ar.apartmentrent.model.Admin;
import com.ar.apartmentrent.model.Lessee;
import com.ar.apartmentrent.model.Lessor;
import com.ar.apartmentrent.model.RefreshToken;
import com.ar.apartmentrent.services.AdminService;
import com.ar.apartmentrent.services.AuthenticationService;
import com.ar.apartmentrent.services.JwtService;
import com.ar.apartmentrent.services.RefreshTokenService;
import com.ar.apartmentrent.services.impl.AdminServiceImpl;
import com.ar.apartmentrent.services.impl.LesseeServiceImpl;
import com.ar.apartmentrent.services.impl.LessorServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "arapi")
public class AuthenticationController {

    private final AuthenticationService service;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final LessorServiceImpl lessorService;
    private final LesseeServiceImpl lesseeService;
    private final AdminServiceImpl adminService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        service.register(request);
        return ResponseEntity.ok("Successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return ResponseEntity.ok(AuthenticationResponse.builder()
                            .token(token)
                            .refreshToken(refreshTokenRequest.getToken())
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Refresh is not in the database!"));
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        try {
            String accessToken = jwtService.resolveToken(request);

            if (accessToken != null) {
                SecurityContextHolder.clearContext();

                jwtService.addToRevokedTokens(accessToken);

                Map<String, String> response = new HashMap<>();
                response.put("message", "Logout successful");

                return ResponseEntity.ok(response);
            }

            throw new InvalidTokenException("Invalid access token");
        } catch (RuntimeException e) {
            throw new LogoutException("Error during logout");
        }
    }
    @GetMapping("/users/{username}")
    public ResponseEntity<Object> getUserByUsername(@Valid @PathVariable String username){
        Lessor lessor = lessorService.getLessorByUsername2(username);
       // log.info("lessor get: " + lessor.getName());
        Lessee lessee = lesseeService.getLesseeByUsername2(username);
      //  log.info("lessee get: " + lessee.getName());

        Admin admin = adminService.getAdminByUsername(username);

        if (Objects.nonNull(lessor)) {
            return ResponseEntity.ok(lessor);
        } else if (Objects.nonNull(lessee)) {
            return ResponseEntity.ok(lessee);
        } else if (Objects.nonNull(admin)) {
            return ResponseEntity.ok(admin);
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

//        try {
//            Lessor lessor = lessorService.getLessorByUsername(username);
//            return ResponseEntity.ok(lessor);
//        }catch (EntityNotFoundException exception){
//            try{
//                Lessee lessee = lesseeService.getLesseeByUsername(username);
//                return ResponseEntity.ok(lessee);
//            }catch (EntityNotFoundException exception1){
//                throw new UsernameNotFoundException("User not found with username: " + username);
//            }
//        }
    }

}
