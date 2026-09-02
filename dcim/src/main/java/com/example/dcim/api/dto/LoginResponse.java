package com.example.dcim.api.dto;
import com.example.dcim.domain.UserRole;
import java.time.Instant;
public record LoginResponse(String accessToken, String tokenType, Instant expiresAt, Long userId,
                            String username, String firstName, String lastName, UserRole role) {}
