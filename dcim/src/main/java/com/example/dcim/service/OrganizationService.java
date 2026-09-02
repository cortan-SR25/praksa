package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrganizationService {
    private final CompanyRepository companies;
    private final OrganizationalUnitRepository organizationalUnits;
    private final ServiceUnitRepository serviceUnits;

    public OrganizationService(CompanyRepository companies, OrganizationalUnitRepository organizationalUnits,
                               ServiceUnitRepository serviceUnits) {
        this.companies = companies; this.organizationalUnits = organizationalUnits; this.serviceUnits = serviceUnits;
    }

    public List<CompanyResponse> companies() { return companies.findAll().stream().map(this::companyResponse).toList(); }
    public CompanyResponse company(Long id) { return companyResponse(findCompany(id)); }
    @Transactional public CompanyResponse createCompany(CompanyDto dto) {
        Company company = new Company(dto.name().trim()); company.setAddress(dto.address()); company.setDescription(dto.description());
        return companyResponse(companies.save(company));
    }
    @Transactional public CompanyResponse updateCompany(Long id, CompanyDto dto) {
        Company company = findCompany(id); company.setName(dto.name().trim()); company.setAddress(dto.address()); company.setDescription(dto.description());
        return companyResponse(company);
    }
    @Transactional public void deleteCompany(Long id) { delete(() -> companies.delete(findCompany(id))); }

    public List<OrganizationalUnitResponse> organizationalUnits(Long companyId) {
        return (companyId == null ? organizationalUnits.findAll() : organizationalUnits.findByCompanyIdOrderByName(companyId))
                .stream().map(this::organizationalUnitResponse).toList();
    }
    public OrganizationalUnitResponse organizationalUnit(Long id) { return organizationalUnitResponse(findOrganizationalUnit(id)); }
    @Transactional public OrganizationalUnitResponse createOrganizationalUnit(OrganizationalUnitDto dto) {
        OrganizationalUnit unit = new OrganizationalUnit(findCompany(dto.companyId()), dto.name().trim()); unit.setDescription(dto.description());
        return organizationalUnitResponse(organizationalUnits.save(unit));
    }
    @Transactional public OrganizationalUnitResponse updateOrganizationalUnit(Long id, OrganizationalUnitDto dto) {
        OrganizationalUnit unit = findOrganizationalUnit(id); unit.setCompany(findCompany(dto.companyId())); unit.setName(dto.name().trim()); unit.setDescription(dto.description());
        return organizationalUnitResponse(unit);
    }
    @Transactional public void deleteOrganizationalUnit(Long id) { delete(() -> organizationalUnits.delete(findOrganizationalUnit(id))); }

    public List<ServiceUnitResponse> serviceUnits(Long organizationalUnitId) {
        return (organizationalUnitId == null ? serviceUnits.findAll() : serviceUnits.findByOrganizationalUnitIdOrderByName(organizationalUnitId))
                .stream().map(this::serviceUnitResponse).toList();
    }
    public ServiceUnitResponse serviceUnit(Long id) { return serviceUnitResponse(findServiceUnit(id)); }
    @Transactional public ServiceUnitResponse createServiceUnit(ServiceUnitDto dto) {
        ServiceUnit unit = new ServiceUnit(findOrganizationalUnit(dto.organizationalUnitId()), dto.name().trim()); unit.setDescription(dto.description());
        return serviceUnitResponse(serviceUnits.save(unit));
    }
    @Transactional public ServiceUnitResponse updateServiceUnit(Long id, ServiceUnitDto dto) {
        ServiceUnit unit = findServiceUnit(id); unit.setOrganizationalUnit(findOrganizationalUnit(dto.organizationalUnitId())); unit.setName(dto.name().trim()); unit.setDescription(dto.description());
        return serviceUnitResponse(unit);
    }
    @Transactional public void deleteServiceUnit(Long id) { delete(() -> serviceUnits.delete(findServiceUnit(id))); }

    private Company findCompany(Long id) { return companies.findById(id).orElseThrow(() -> new ResourceNotFoundException("Kompanija", id)); }
    private OrganizationalUnit findOrganizationalUnit(Long id) { return organizationalUnits.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organizaciona jedinica", id)); }
    private ServiceUnit findServiceUnit(Long id) { return serviceUnits.findById(id).orElseThrow(() -> new ResourceNotFoundException("Servisna jedinica", id)); }
    private CompanyResponse companyResponse(Company c) { return new CompanyResponse(c.getId(), c.getName(), c.getAddress(), c.getDescription()); }
    private OrganizationalUnitResponse organizationalUnitResponse(OrganizationalUnit u) { return new OrganizationalUnitResponse(u.getId(), u.getCompany().getId(), u.getCompany().getName(), u.getName(), u.getDescription()); }
    private ServiceUnitResponse serviceUnitResponse(ServiceUnit u) { return new ServiceUnitResponse(u.getId(), u.getOrganizationalUnit().getId(), u.getOrganizationalUnit().getName(), u.getName(), u.getDescription()); }
    private void delete(Runnable action) { try { action.run(); companies.flush(); } catch (DataIntegrityViolationException e) { throw new BusinessRuleException("Resurs se ne može obrisati jer postoje povezani podaci."); } }
}
