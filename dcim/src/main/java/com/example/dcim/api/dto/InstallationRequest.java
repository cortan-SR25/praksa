package com.example.dcim.api.dto;
import com.example.dcim.domain.InstallationStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record InstallationRequest(@NotNull Long deviceId, @NotNull Long softwareId, Long licenseId,
        LocalDate installationDate, @Size(max=100) String installedVersion,
        @NotNull InstallationStatus status) {}
