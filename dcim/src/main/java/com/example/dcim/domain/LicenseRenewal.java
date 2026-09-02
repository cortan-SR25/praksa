package com.example.dcim.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "license_renewals")
public class LicenseRenewal extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "license_id", nullable = false)
    private SoftwareLicense license;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "renewed_by_user_id", nullable = false)
    private ApplicationUser renewedBy;
    @Column(name = "previous_end_date")
    private LocalDate previousEndDate;
    @Column(name = "new_end_date", nullable = false)
    private LocalDate newEndDate;
    @Column(name = "renewed_at", nullable = false, updatable = false)
    private Instant renewedAt = Instant.now();
    @Column(columnDefinition = "TEXT")
    private String note;

    protected LicenseRenewal() {}
    public LicenseRenewal(SoftwareLicense license, ApplicationUser renewedBy, LocalDate previousEndDate,
                          LocalDate newEndDate, String note) {
        this.license=license; this.renewedBy=renewedBy; this.previousEndDate=previousEndDate;
        this.newEndDate=newEndDate; this.note=note;
    }
    public SoftwareLicense getLicense() { return license; }
    public ApplicationUser getRenewedBy() { return renewedBy; }
    public LocalDate getPreviousEndDate() { return previousEndDate; }
    public LocalDate getNewEndDate() { return newEndDate; }
    public Instant getRenewedAt() { return renewedAt; }
    public String getNote() { return note; }
}
