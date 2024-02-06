package com.ar.apartmentrent.config;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration implements WebMvcConfigurer {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui.html",
            "/error"
    };
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.anyRequest().permitAll())
//                        authorizationManagerRequestMatcherRegistry.requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/profile/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/*/lessees/*/rentcases/*").hasAnyAuthority("LESSOR", "ADMIN", "LESSEE")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors").hasAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/*/lessees").hasAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/*/lessees/*/rentcases").hasAnyAuthority("LESSOR", "ADMIN", "LESSEE")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessors/*/lessees/*").hasAnyAuthority("LESSEE", "ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/lessees/profile/*").hasAnyAuthority("LESSEE", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/lessors/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/lessors/*/rentcases/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/lessors/*/lessees/*/rentcases/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/lessors/*/lessees/*").hasAnyAuthority("LESSEE", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/v1/lessors/*/lessees/").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/v1/lessors/*/lessees/*/rentcases/").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/v1/lessors/").hasAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/lessors/*/lesssees/*/rentcases/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/lessors/*/lessees/*").hasAnyAuthority("LESSOR", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/lessors/*").hasAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/lessors/*/lessees/*").hasAuthority("ADMIN")
//                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException)
                                -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                        .authenticationEntryPoint((request, response, authException)
                                -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                );
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }
}
