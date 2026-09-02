package com.example.dcim.api;

import com.example.dcim.api.dto.*;
import com.example.dcim.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/service-units")
public class ServiceUnitController {
    private final OrganizationService service;
    public ServiceUnitController(OrganizationService service) { this.service = service; }
    @GetMapping public List<ServiceUnitResponse> list(@RequestParam(required=false) Long organizationalUnitId) { return service.serviceUnits(organizationalUnitId); }
    @GetMapping("/{id}") public ServiceUnitResponse get(@PathVariable Long id) { return service.serviceUnit(id); }
    @PostMapping public ResponseEntity<ServiceUnitResponse> create(@Valid @RequestBody ServiceUnitDto dto) {
        var created = service.createServiceUnit(dto); return ResponseEntity.created(URI.create("/api/service-units/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public ServiceUnitResponse update(@PathVariable Long id, @Valid @RequestBody ServiceUnitDto dto) { return service.updateServiceUnit(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteServiceUnit(id); }
}
