package com.example.dcim.api.dto;
import com.example.dcim.domain.SoftwareType;
public record SoftwareResponse(Long id, String name, String vendor, String version, SoftwareType softwareType, String description) {}
