package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class SoftwareCatalogService {
    private final SoftwareRepository softwareRepository;
    private final SoftwareLicenseRepository licenseRepository;
    public SoftwareCatalogService(SoftwareRepository softwareRepository, SoftwareLicenseRepository licenseRepository) {
        this.softwareRepository = softwareRepository; this.licenseRepository = licenseRepository;
    }
    public List<SoftwareResponse> software() { return softwareRepository.findAll().stream().map(this::softwareResponse).toList(); }
    public SoftwareResponse software(Long id) { return softwareResponse(findSoftware(id)); }
    @Transactional public SoftwareResponse createSoftware(SoftwareDto dto) {
        Software software = new Software(dto.name().trim(), dto.vendor().trim(), dto.version().trim(), dto.softwareType());
        software.update(dto.name().trim(), dto.vendor().trim(), dto.version().trim(), dto.softwareType(), dto.description());
        return softwareResponse(softwareRepository.save(software));
    }
    @Transactional public SoftwareResponse updateSoftware(Long id, SoftwareDto dto) {
        Software software = findSoftware(id); software.update(dto.name().trim(), dto.vendor().trim(), dto.version().trim(), dto.softwareType(), dto.description());
        return softwareResponse(software);
    }
    @Transactional public void deleteSoftware(Long id) { try { softwareRepository.delete(findSoftware(id)); softwareRepository.flush(); } catch (DataIntegrityViolationException e) { throw new BusinessRuleException("Softver se ne može obrisati dok ima licence ili instalacije."); } }

    public List<SoftwareLicenseResponse> licenses(LocalDate expiringFrom, LocalDate expiringTo) {
        if ((expiringFrom == null) != (expiringTo == null)) throw new BusinessRuleException("Oba datuma filtera moraju biti prosleđena.");
        if (expiringFrom != null && expiringTo.isBefore(expiringFrom)) throw new BusinessRuleException("Krajnji datum filtera mora biti posle početnog.");
        List<SoftwareLicense> result = expiringFrom == null ? licenseRepository.findAll() : licenseRepository.findByEndDateBetweenOrderByEndDateAsc(expiringFrom, expiringTo);
        return result.stream().map(this::licenseResponse).toList();
    }
    public SoftwareLicenseResponse license(Long id) { return licenseResponse(findLicense(id)); }
    @Transactional public SoftwareLicenseResponse createLicense(SoftwareLicenseDto dto) {
        validateLicense(dto); SoftwareLicense license = new SoftwareLicense(findSoftware(dto.softwareId()), dto.licenseType(), dto.startDate(), dto.endDate(), dto.quantity());
        license.update(findSoftware(dto.softwareId()), dto.licenseKey(), dto.licenseType(), dto.startDate(), dto.endDate(), dto.quantity(), dto.purchasePrice(), dto.notes());
        return licenseResponse(licenseRepository.save(license));
    }
    @Transactional public SoftwareLicenseResponse updateLicense(Long id, SoftwareLicenseDto dto) {
        validateLicense(dto); SoftwareLicense license = findLicense(id);
        if (!Objects.equals(license.getEndDate(), dto.endDate()))
            throw new BusinessRuleException("Datum isteka menja se preko operacije za obnavljanje licence.");
        license.update(findSoftware(dto.softwareId()), dto.licenseKey(), dto.licenseType(), dto.startDate(), dto.endDate(), dto.quantity(), dto.purchasePrice(), dto.notes());
        return licenseResponse(license);
    }
    @Transactional public void deleteLicense(Long id) { try { licenseRepository.delete(findLicense(id)); licenseRepository.flush(); } catch (DataIntegrityViolationException e) { throw new BusinessRuleException("Licenca se ne može obrisati dok je povezana sa instalacijom ili obnovom."); } }

    private void validateLicense(SoftwareLicenseDto dto) {
        if (dto.endDate() != null && dto.endDate().isBefore(dto.startDate())) throw new BusinessRuleException("Datum isteka ne može biti pre datuma početka.");
        if (dto.licenseType() != LicenseType.PERPETUAL && dto.endDate() == null) throw new BusinessRuleException("Pretplatnička i probna licenca moraju imati datum isteka.");
    }
    private Software findSoftware(Long id) { return softwareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Softver", id)); }
    private SoftwareLicense findLicense(Long id) { return licenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Licenca", id)); }
    private SoftwareResponse softwareResponse(Software s) { return new SoftwareResponse(s.getId(), s.getName(), s.getVendor(), s.getVersion(), s.getSoftwareType(), s.getDescription()); }
    private SoftwareLicenseResponse licenseResponse(SoftwareLicense l) {
        LocalDate today = LocalDate.now(); String status = l.getEndDate() == null ? "PERPETUAL" : l.getStartDate().isAfter(today) ? "UPCOMING" : l.getEndDate().isBefore(today) ? "EXPIRED" : !l.getEndDate().isAfter(today.plusDays(30)) ? "EXPIRING_SOON" : "ACTIVE";
        return new SoftwareLicenseResponse(l.getId(), l.getSoftware().getId(), l.getSoftware().getName(), l.getLicenseKey(), l.getLicenseType(), l.getStartDate(), l.getEndDate(), l.getQuantity(), l.getPurchasePrice(), l.getNotes(), status);
    }
}
