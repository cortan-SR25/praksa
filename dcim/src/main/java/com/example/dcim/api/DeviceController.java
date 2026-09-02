package com.example.dcim.api;
import com.example.dcim.api.dto.*;
import com.example.dcim.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/devices")
public class DeviceController {
    private final AssetService service;
    public DeviceController(AssetService service){this.service=service;}
    @GetMapping public List<DeviceResponse> list(@RequestParam(required=false) Long serviceUnitId,@RequestParam(required=false) Long responsibleUserId){return service.devices(serviceUnitId,responsibleUserId);}
    @GetMapping("/{id}") public DeviceResponse get(@PathVariable Long id){return service.device(id);}
    @PostMapping public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceRequest request){var created=service.createDevice(request);return ResponseEntity.created(URI.create("/api/devices/"+created.id())).body(created);}
    @PutMapping("/{id}") public DeviceResponse update(@PathVariable Long id,@Valid @RequestBody DeviceRequest request){return service.updateDevice(id,request);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.deleteDevice(id);}
}
