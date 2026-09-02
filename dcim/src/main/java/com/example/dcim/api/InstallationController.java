package com.example.dcim.api;
import com.example.dcim.api.dto.*;
import com.example.dcim.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/installations")
public class InstallationController {
    private final AssetService service;
    public InstallationController(AssetService service){this.service=service;}
    @GetMapping public List<InstallationResponse> list(@RequestParam(required=false) Long deviceId){return service.installations(deviceId);}
    @GetMapping("/{id}") public InstallationResponse get(@PathVariable Long id){return service.installation(id);}
    @PostMapping public ResponseEntity<InstallationResponse> create(@Valid @RequestBody InstallationRequest request){var created=service.createInstallation(request);return ResponseEntity.created(URI.create("/api/installations/"+created.id())).body(created);}
    @PutMapping("/{id}") public InstallationResponse update(@PathVariable Long id,@Valid @RequestBody InstallationRequest request){return service.updateInstallation(id,request);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.deleteInstallation(id);}
}
