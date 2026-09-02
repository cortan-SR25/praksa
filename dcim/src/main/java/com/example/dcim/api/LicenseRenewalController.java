package com.example.dcim.api;
import com.example.dcim.api.dto.*;
import com.example.dcim.service.LicenseRenewalService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/licenses/{licenseId}/renewals")
public class LicenseRenewalController {
    private final LicenseRenewalService service;
    public LicenseRenewalController(LicenseRenewalService service){this.service=service;}
    @GetMapping @PreAuthorize("hasRole('ADMIN') or @licenseAuthorization.canManage(#licenseId, authentication.name)")
    public List<LicenseRenewalResponse> history(@PathVariable Long licenseId){return service.history(licenseId);}
    @PostMapping @PreAuthorize("hasRole('ADMIN') or @licenseAuthorization.canManage(#licenseId, authentication.name)")
    public ResponseEntity<LicenseRenewalResponse> renew(@PathVariable Long licenseId,@Valid @RequestBody LicenseRenewalRequest request,Authentication authentication){
        var created=service.renew(licenseId,request,authentication.getName());
        return ResponseEntity.created(URI.create("/api/licenses/"+licenseId+"/renewals/"+created.id())).body(created);
    }
}
