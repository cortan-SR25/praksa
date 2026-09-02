package com.example.dcim.api.dto;
import com.example.dcim.domain.*;
import java.time.Instant;
public record DeviceResponse(Long id, Long serviceUnitId, String serviceUnitName, Long responsibleUserId,
        String responsibleUserName, String name, String hostname, String ipAddress, String serialNumber,
        String manufacturer, String model, DeviceType deviceType, DeviceStatus status, Instant createdAt) {}
