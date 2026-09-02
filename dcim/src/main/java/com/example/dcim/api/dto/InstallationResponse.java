package com.example.dcim.api.dto;
import com.example.dcim.domain.InstallationStatus;
import java.time.LocalDate;
public record InstallationResponse(Long id, Long deviceId, String deviceName, Long softwareId,
        String softwareName, Long licenseId, LocalDate installationDate, String installedVersion,
        InstallationStatus status) {}
