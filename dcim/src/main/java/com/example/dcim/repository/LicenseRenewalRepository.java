package com.example.dcim.repository;
import com.example.dcim.domain.LicenseRenewal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LicenseRenewalRepository extends JpaRepository<LicenseRenewal,Long> {
    List<LicenseRenewal> findByLicenseIdOrderByRenewedAtDesc(Long licenseId);
}
