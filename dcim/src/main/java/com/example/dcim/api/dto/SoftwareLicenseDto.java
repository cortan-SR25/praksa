package com.example.dcim.api.dto;
import com.example.dcim.domain.LicenseType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record SoftwareLicenseDto(@NotNull Long softwareId,
                                 @Size(max=255) String licenseKey,
                                 @NotNull LicenseType licenseType,
                                 @NotNull LocalDate startDate,
                                 LocalDate endDate,
                                 @Min(1) int quantity,
                                 @DecimalMin("0.00") BigDecimal purchasePrice,
                                 String notes) {}
