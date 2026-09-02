package com.example.dcim.api.dto;
import com.example.dcim.domain.LicenseType;
import java.math.BigDecimal;
import java.time.LocalDate;
public record SoftwareLicenseResponse(Long id, Long softwareId, String softwareName, String licenseKey,
                                      LicenseType licenseType, LocalDate startDate, LocalDate endDate,
                                      int quantity, BigDecimal purchasePrice, String notes, String computedStatus) {}
