package com.example.dcim.security;
import com.example.dcim.domain.InstallationStatus;
import com.example.dcim.repository.SoftwareInstallationRepository;
import org.springframework.stereotype.Component;
@Component
public class LicenseAuthorization {
    private final SoftwareInstallationRepository installations;
    public LicenseAuthorization(SoftwareInstallationRepository installations){this.installations=installations;}
    public boolean canManage(Long licenseId,String username){
        return installations.existsByLicenseIdAndStatusAndDeviceResponsibleUserUsernameIgnoreCase(
                licenseId, InstallationStatus.INSTALLED, username);
    }
}
