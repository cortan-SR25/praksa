package com.example.dcim.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "organizational_units", uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "name"}))
public class OrganizationalUnit extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    protected OrganizationalUnit() {}
    public OrganizationalUnit(Company company, String name) { this.company = company; this.name = name; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
