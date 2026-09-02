package com.example.dcim.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "software", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "vendor", "version"}))
public class Software extends BaseEntity {
    @Column(nullable = false, length = 150)
    private String name;
    @Column(nullable = false, length = 150)
    private String vendor;
    @Column(nullable = false, length = 100)
    private String version;
    @Enumerated(EnumType.STRING)
    @Column(name = "software_type", nullable = false, length = 30)
    private SoftwareType softwareType;
    @Column(columnDefinition = "TEXT")
    private String description;

    protected Software() {}
    public Software(String name, String vendor, String version, SoftwareType softwareType) {
        this.name = name;
        this.vendor = vendor;
        this.version = version;
        this.softwareType = softwareType;
    }
    public String getName() { return name; }
    public String getVendor() { return vendor; }
    public String getVersion() { return version; }
    public SoftwareType getSoftwareType() { return softwareType; }
    public String getDescription() { return description; }
    public void update(String name, String vendor, String version, SoftwareType softwareType, String description) {
        this.name = name;
        this.vendor = vendor;
        this.version = version;
        this.softwareType = softwareType;
        this.description = description;
    }
}
