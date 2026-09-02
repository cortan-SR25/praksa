package com.example.dcim.api.dto;
import com.example.dcim.domain.UserRole;
import java.time.Instant;
public record UserResponse(Long id, Long serviceUnitId, String serviceUnitName, String username,
                           String firstName, String lastName, String email, UserRole role,
                           boolean active, Instant createdAt) {}
