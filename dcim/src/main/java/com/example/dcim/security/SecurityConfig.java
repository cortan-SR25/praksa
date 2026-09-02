package com.example.dcim.security;

import com.example.dcim.api.ApiError;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean SecretKey jwtSecretKey(@Value("${dcim.jwt.secret}") String secret) {
        byte[] decoded;
        try { decoded = Base64.getDecoder().decode(secret); }
        catch (IllegalArgumentException e) { throw new IllegalStateException("JWT_SECRET mora biti Base64 vrednost.", e); }
        if (decoded.length < 32) throw new IllegalStateException("JWT_SECRET mora sadržati najmanje 32 bajta.");
        return new SecretKeySpec(decoded, "HmacSHA256");
    }
    @Bean JwtEncoder jwtEncoder(SecretKey key) { return NimbusJwtEncoder.withSecretKey(key).build(); }
    @Bean JwtDecoder jwtDecoder(SecretKey key) { return NimbusJwtDecoder.withSecretKey(key).build(); }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper mapper) throws Exception {
        return http.csrf(csrf -> csrf.disable()).cors(cors -> {})
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/licenses/*/renewals").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/licenses/*/renewals").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/licenses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/licenses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/licenses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/companies/**","/api/organizational-units/**","/api/service-units/**","/api/software/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/companies/**","/api/organizational-units/**","/api/service-units/**","/api/software/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/companies/**","/api/organizational-units/**","/api/service-units/**","/api/software/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/devices/**","/api/installations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/devices/**","/api/installations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/devices/**","/api/installations/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtRoleConverter()))
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(401); response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            mapper.writeValue(response.getOutputStream(), new ApiError(Instant.now(), 401, "Unauthorized", "Prijava je potrebna ili je token nevažeći.", Map.of()));
                        }))
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(403); response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getOutputStream(), new ApiError(Instant.now(), 403, "Forbidden", "Nemate dozvolu za ovu operaciju.", Map.of()));
                })).build();
    }
}
