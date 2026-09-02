package com.example.dcim.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "service_units", uniqueConstraints = @UniqueConstraint(columnNames = {"organizational_unit_id", "name"}))
public class ServiceUnit extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizational_unit_id", nullable = false)
    private OrganizationalUnit organizationalUnit;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    protected ServiceUnit() {}
    public ServiceUnit(OrganizationalUnit organizationalUnit, String name) { this.organizationalUnit = organizationalUnit; this.name = name; }
    public OrganizationalUnit getOrganizationalUnit() { return organizationalUnit; }
    public void setOrganizationalUnit(OrganizationalUnit organizationalUnit) { this.organizationalUnit = organizationalUnit; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
