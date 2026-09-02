package com.example.dcim.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "devices")
public class Device extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_unit_id", nullable = false)
    private ServiceUnit serviceUnit;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_user_id", nullable = false)
    private ApplicationUser responsibleUser;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(unique = true)
    private String hostname;
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    @Column(name = "serial_number", unique = true, length = 100)
    private String serialNumber;
    @Column(length = 100)
    private String manufacturer;
    @Column(length = 100)
    private String model;
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 30)
    private DeviceType deviceType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status = DeviceStatus.ACTIVE;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Device() {}
    public Device(ServiceUnit serviceUnit, ApplicationUser responsibleUser, String name, DeviceType deviceType) {
        this.serviceUnit = serviceUnit; this.responsibleUser = responsibleUser; this.name = name; this.deviceType = deviceType;
    }
    public ServiceUnit getServiceUnit() { return serviceUnit; }
    public ApplicationUser getResponsibleUser() { return responsibleUser; }
    public String getName() { return name; }
    public DeviceType getDeviceType() { return deviceType; }
    public DeviceStatus getStatus() { return status; }
    public String getHostname() { return hostname; }
    public String getIpAddress() { return ipAddress; }
    public String getSerialNumber() { return serialNumber; }
    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public Instant getCreatedAt() { return createdAt; }
    public void update(ServiceUnit serviceUnit, ApplicationUser responsibleUser, String name, String hostname,
                       String ipAddress, String serialNumber, String manufacturer, String model,
                       DeviceType deviceType, DeviceStatus status) {
        this.serviceUnit=serviceUnit; this.responsibleUser=responsibleUser; this.name=name; this.hostname=hostname;
        this.ipAddress=ipAddress; this.serialNumber=serialNumber; this.manufacturer=manufacturer; this.model=model;
        this.deviceType=deviceType; this.status=status;
    }
}
