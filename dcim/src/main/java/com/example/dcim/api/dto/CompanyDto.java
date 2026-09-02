package com.example.dcim.api.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CompanyDto(@NotBlank @Size(max=150) String name, @Size(max=255) String address, String description) {}
