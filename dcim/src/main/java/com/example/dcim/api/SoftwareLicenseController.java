package com.example.dcim.api;

import com.example.dcim.api.dto.*;
import com.example.dcim.service.SoftwareCatalogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class SoftwareLicenseController {
    private final SoftwareCatalogService service;
    public SoftwareLicenseController(SoftwareCatalogService service) { this.service = service; }
    @GetMapping public List<SoftwareLicenseResponse> list(
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate expiringFrom,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate expiringTo) {
        return service.licenses(expiringFrom, expiringTo);
    }
    @GetMapping("/{id}") public SoftwareLicenseResponse get(@PathVariable Long id) { return service.license(id); }
    @PostMapping public ResponseEntity<SoftwareLicenseResponse> create(@Valid @RequestBody SoftwareLicenseDto dto) {
        var created = service.createLicense(dto); return ResponseEntity.created(URI.create("/api/licenses/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public SoftwareLicenseResponse update(@PathVariable Long id, @Valid @RequestBody SoftwareLicenseDto dto) { return service.updateLicense(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteLicense(id); }
}
