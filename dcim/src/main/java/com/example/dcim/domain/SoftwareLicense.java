package com.example.dcim.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "software_licenses")
public class SoftwareLicense extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;
    @Column(name = "license_key")
    private String licenseKey;
    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", nullable = false, length = 20)
    private LicenseType licenseType;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(nullable = false)
    private int quantity = 1;
    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;
    @Column(columnDefinition = "TEXT")
    private String notes;

    protected SoftwareLicense() {}
    public SoftwareLicense(Software software, LicenseType licenseType, LocalDate startDate, LocalDate endDate, int quantity) {
        this.software = software;
        this.licenseType = licenseType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
    }
    public Software getSoftware() { return software; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getQuantity() { return quantity; }
    public String getLicenseKey() { return licenseKey; }
    public LicenseType getLicenseType() { return licenseType; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public String getNotes() { return notes; }
    public void update(Software software, String licenseKey, LicenseType licenseType, LocalDate startDate,
                       LocalDate endDate, int quantity, BigDecimal purchasePrice, String notes) {
        this.software = software;
        this.licenseKey = licenseKey;
        this.licenseType = licenseType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.notes = notes;
    }
    public void renewUntil(LocalDate newEndDate) { this.endDate = newEndDate; }
    public boolean expiresBetween(LocalDate from, LocalDate to) { return endDate != null && !endDate.isBefore(from) && !endDate.isAfter(to); }
}
