package com.example.dcim.api.dto;
import com.example.dcim.domain.SoftwareType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record SoftwareDto(@NotBlank @Size(max=150) String name,
                          @NotBlank @Size(max=150) String vendor,
                          @NotBlank @Size(max=100) String version,
                          @NotNull SoftwareType softwareType,
                          String description) {}
