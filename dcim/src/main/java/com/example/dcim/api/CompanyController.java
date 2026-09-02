package com.example.dcim.api;

import com.example.dcim.api.dto.*;
import com.example.dcim.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final OrganizationService service;
    public CompanyController(OrganizationService service) { this.service = service; }
    @GetMapping public List<CompanyResponse> list() { return service.companies(); }
    @GetMapping("/{id}") public CompanyResponse get(@PathVariable Long id) { return service.company(id); }
    @PostMapping public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyDto dto) {
        CompanyResponse created = service.createCompany(dto); return ResponseEntity.created(URI.create("/api/companies/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyDto dto) { return service.updateCompany(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteCompany(id); }
}
