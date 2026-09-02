package com.example.dcim.api;

import com.example.dcim.api.dto.*;
import com.example.dcim.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/organizational-units")
public class OrganizationalUnitController {
    private final OrganizationService service;
    public OrganizationalUnitController(OrganizationService service) { this.service = service; }
    @GetMapping public List<OrganizationalUnitResponse> list(@RequestParam(required=false) Long companyId) { return service.organizationalUnits(companyId); }
    @GetMapping("/{id}") public OrganizationalUnitResponse get(@PathVariable Long id) { return service.organizationalUnit(id); }
    @PostMapping public ResponseEntity<OrganizationalUnitResponse> create(@Valid @RequestBody OrganizationalUnitDto dto) {
        var created = service.createOrganizationalUnit(dto); return ResponseEntity.created(URI.create("/api/organizational-units/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public OrganizationalUnitResponse update(@PathVariable Long id, @Valid @RequestBody OrganizationalUnitDto dto) { return service.updateOrganizationalUnit(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteOrganizationalUnit(id); }
}
