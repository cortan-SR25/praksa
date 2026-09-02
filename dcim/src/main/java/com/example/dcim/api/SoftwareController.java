package com.example.dcim.api;

import com.example.dcim.api.dto.*;
import com.example.dcim.service.SoftwareCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/software")
public class SoftwareController {
    private final SoftwareCatalogService service;
    public SoftwareController(SoftwareCatalogService service) { this.service = service; }
    @GetMapping public List<SoftwareResponse> list() { return service.software(); }
    @GetMapping("/{id}") public SoftwareResponse get(@PathVariable Long id) { return service.software(id); }
    @PostMapping public ResponseEntity<SoftwareResponse> create(@Valid @RequestBody SoftwareDto dto) {
        var created = service.createSoftware(dto); return ResponseEntity.created(URI.create("/api/software/" + created.id())).body(created);
    }
    @PutMapping("/{id}") public SoftwareResponse update(@PathVariable Long id, @Valid @RequestBody SoftwareDto dto) { return service.updateSoftware(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteSoftware(id); }
}
