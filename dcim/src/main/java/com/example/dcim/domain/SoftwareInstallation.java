package com.example.dcim.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "software_installations", uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "software_id"}))
public class SoftwareInstallation extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id")
    private SoftwareLicense license;
    @Column(name = "installation_date")
    private LocalDate installationDate;
    @Column(name = "installed_version", length = 100)
    private String installedVersion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InstallationStatus status = InstallationStatus.INSTALLED;

    protected SoftwareInstallation() {}
    public SoftwareInstallation(Device device, Software software) { this.device = device; this.software = software; }
    public Device getDevice() { return device; }
    public Software getSoftware() { return software; }
    public SoftwareLicense getLicense() { return license; }
    public InstallationStatus getStatus() { return status; }
    public LocalDate getInstallationDate() { return installationDate; }
    public String getInstalledVersion() { return installedVersion; }
    public void update(Device device, Software software, SoftwareLicense license, LocalDate installationDate,
                       String installedVersion, InstallationStatus status) {
        this.device=device; this.software=software; this.license=license; this.installationDate=installationDate;
        this.installedVersion=installedVersion; this.status=status;
    }
}
