package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class LicenseRenewalService {
    private final SoftwareLicenseRepository licenses; private final LicenseRenewalRepository renewals;
    private final ApplicationUserRepository users;
    public LicenseRenewalService(SoftwareLicenseRepository licenses,LicenseRenewalRepository renewals,ApplicationUserRepository users){
        this.licenses=licenses;this.renewals=renewals;this.users=users;
    }
    public List<LicenseRenewalResponse> history(Long licenseId){
        if(!licenses.existsById(licenseId))throw new ResourceNotFoundException("Licenca",licenseId);
        return renewals.findByLicenseIdOrderByRenewedAtDesc(licenseId).stream().map(this::response).toList();
    }
    @Transactional public LicenseRenewalResponse renew(Long licenseId,LicenseRenewalRequest request,String username){
        SoftwareLicense license=licenses.findByIdForUpdate(licenseId).orElseThrow(()->new ResourceNotFoundException("Licenca",licenseId));
        ApplicationUser user=users.findByUsernameIgnoreCase(username).orElseThrow(InvalidCredentialsException::new);
        LocalDate previous=license.getEndDate();
        if(license.getLicenseType()==LicenseType.PERPETUAL)throw new BusinessRuleException("Trajna licenca nema datum isteka i ne obnavlja se.");
        if(previous==null)throw new BusinessRuleException("Licenca nema postojeći datum isteka.");
        if(!request.newEndDate().isAfter(previous))throw new BusinessRuleException("Novi datum isteka mora biti posle trenutnog datuma isteka.");
        LicenseRenewal renewal=new LicenseRenewal(license,user,previous,request.newEndDate(),blankToNull(request.note()));
        license.renewUntil(request.newEndDate());
        return response(renewals.save(renewal));
    }
    private String blankToNull(String value){return value==null||value.isBlank()?null:value.trim();}
    private LicenseRenewalResponse response(LicenseRenewal r){ApplicationUser u=r.getRenewedBy();return new LicenseRenewalResponse(
            r.getId(),r.getLicense().getId(),u.getId(),u.getFirstName()+" "+u.getLastName(),r.getPreviousEndDate(),r.getNewEndDate(),r.getRenewedAt(),r.getNote());}
}
