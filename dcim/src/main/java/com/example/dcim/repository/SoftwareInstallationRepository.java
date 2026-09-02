package com.example.dcim.repository;
import com.example.dcim.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SoftwareInstallationRepository extends JpaRepository<SoftwareInstallation, Long> {
    List<SoftwareInstallation> findByDeviceIdOrderBySoftwareName(Long deviceId);
    long countByLicenseIdAndStatus(Long licenseId, InstallationStatus status);
    long countByLicenseIdAndStatusAndIdNot(Long licenseId, InstallationStatus status, Long id);
    boolean existsByLicenseIdAndStatusAndDeviceResponsibleUserUsernameIgnoreCase(Long licenseId, InstallationStatus status, String username);
}
