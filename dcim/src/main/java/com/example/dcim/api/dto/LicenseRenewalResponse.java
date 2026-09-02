package com.example.dcim.api.dto;
import java.time.*;
public record LicenseRenewalResponse(Long id, Long licenseId, Long renewedByUserId, String renewedByName,
        LocalDate previousEndDate, LocalDate newEndDate, Instant renewedAt, String note) {}
