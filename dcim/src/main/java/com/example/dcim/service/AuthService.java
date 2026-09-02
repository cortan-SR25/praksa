package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.ApplicationUser;
import com.example.dcim.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final ApplicationUserRepository users; private final PasswordEncoder passwords;
    private final JwtEncoder jwtEncoder; private final Duration expiration;
    public AuthService(ApplicationUserRepository users, PasswordEncoder passwords, JwtEncoder jwtEncoder,
                       @Value("${dcim.jwt.expiration}") Duration expiration) {
        this.users = users; this.passwords = passwords; this.jwtEncoder = jwtEncoder; this.expiration = expiration;
    }
    public LoginResponse login(LoginRequest request) {
        ApplicationUser user = users.findByUsernameIgnoreCase(request.username().trim()).orElseThrow(InvalidCredentialsException::new);
        if (!user.isActive() || !passwords.matches(request.password(), user.getPasswordHash())) throw new InvalidCredentialsException();
        Instant now = Instant.now(), expiresAt = now.plus(expiration);
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("dcim-api").issuedAt(now).expiresAt(expiresAt)
                .subject(user.getUsername()).claim("uid", user.getId()).claim("roles", List.of("ROLE_" + user.getRole().name())).build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new LoginResponse(token, "Bearer", expiresAt, user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getRole());
    }
}
