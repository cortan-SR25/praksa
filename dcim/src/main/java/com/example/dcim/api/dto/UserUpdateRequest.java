package com.example.dcim.api.dto;
import com.example.dcim.domain.UserRole;
import jakarta.validation.constraints.*;
public record UserUpdateRequest(@NotNull Long serviceUnitId,
                                @NotBlank @Size(min=3,max=50) String username,
                                @NotBlank @Size(max=100) String firstName,
                                @NotBlank @Size(max=100) String lastName,
                                @NotBlank @Email @Size(max=150) String email,
                                @NotNull UserRole role,
                                boolean active) {}
