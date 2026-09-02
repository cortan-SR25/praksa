package com.example.dcim.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record OrganizationalUnitDto(@NotNull Long companyId, @NotBlank @Size(max=150) String name, String description) {}
