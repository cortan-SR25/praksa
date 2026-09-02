package com.example.dcim.api.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record LicenseRenewalRequest(@NotNull @Future LocalDate newEndDate, @Size(max=2000) String note) {}
