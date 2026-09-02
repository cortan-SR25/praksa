package com.example.dcim.api.dto;
import com.example.dcim.domain.*;
import jakarta.validation.constraints.*;
public record DeviceRequest(@NotNull Long serviceUnitId, @NotNull Long responsibleUserId,
        @NotBlank @Size(max=150) String name, @Size(max=255) String hostname,
        @Size(max=45) String ipAddress, @Size(max=100) String serialNumber,
        @Size(max=100) String manufacturer, @Size(max=100) String model,
        @NotNull DeviceType deviceType, @NotNull DeviceStatus status) {}
