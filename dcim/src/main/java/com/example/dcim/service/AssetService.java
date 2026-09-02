package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssetService {
    private final DeviceRepository devices; private final ServiceUnitRepository serviceUnits;
    private final ApplicationUserRepository users; private final SoftwareRepository software;
    private final SoftwareLicenseRepository licenses; private final SoftwareInstallationRepository installations;
    public AssetService(DeviceRepository devices, ServiceUnitRepository serviceUnits, ApplicationUserRepository users,
                        SoftwareRepository software, SoftwareLicenseRepository licenses, SoftwareInstallationRepository installations) {
        this.devices=devices; this.serviceUnits=serviceUnits; this.users=users; this.software=software;
        this.licenses=licenses; this.installations=installations;
    }

    public List<DeviceResponse> devices(Long serviceUnitId, Long responsibleUserId) {
        if (serviceUnitId != null && responsibleUserId != null) throw new BusinessRuleException("Koristite samo jedan filter uređaja.");
        List<Device> result = serviceUnitId != null ? devices.findByServiceUnitIdOrderByName(serviceUnitId)
                : responsibleUserId != null ? devices.findByResponsibleUserIdOrderByName(responsibleUserId) : devices.findAll();
        return result.stream().map(this::deviceResponse).toList();
    }
    public DeviceResponse device(Long id) { return deviceResponse(findDevice(id)); }
    @Transactional public DeviceResponse createDevice(DeviceRequest dto) {
        ServiceUnit unit=findServiceUnit(dto.serviceUnitId()); ApplicationUser responsible=findUser(dto.responsibleUserId());
        validateResponsible(unit, responsible);
        Device device=new Device(unit, responsible, dto.name().trim(), dto.deviceType()); updateDevice(device,dto,unit,responsible);
        return deviceResponse(saveDevice(device));
    }
    @Transactional public DeviceResponse updateDevice(Long id, DeviceRequest dto) {
        Device device=findDevice(id); ServiceUnit unit=findServiceUnit(dto.serviceUnitId()); ApplicationUser responsible=findUser(dto.responsibleUserId());
        validateResponsible(unit,responsible); updateDevice(device,dto,unit,responsible); return deviceResponse(saveDevice(device));
    }
    @Transactional public void deleteDevice(Long id) {
        try { devices.delete(findDevice(id)); devices.flush(); }
        catch (DataIntegrityViolationException e) { throw new BusinessRuleException("Uređaj ima evidentirane instalacije. Označite ga kao RETIRED umesto brisanja."); }
    }

    public List<InstallationResponse> installations(Long deviceId) {
        List<SoftwareInstallation> result=deviceId==null?installations.findAll():installations.findByDeviceIdOrderBySoftwareName(deviceId);
        return result.stream().map(this::installationResponse).toList();
    }
    public InstallationResponse installation(Long id) { return installationResponse(findInstallation(id)); }
    @Transactional public InstallationResponse createInstallation(InstallationRequest dto) {
        Device device=findDevice(dto.deviceId()); Software product=findSoftware(dto.softwareId()); SoftwareLicense license=findOptionalLicense(dto.licenseId());
        validateInstallation(null,product,license,dto.status());
        SoftwareInstallation installation=new SoftwareInstallation(device,product);
        installation.update(device,product,license,dto.installationDate(),dto.installedVersion(),dto.status());
        return installationResponse(saveInstallation(installation));
    }
    @Transactional public InstallationResponse updateInstallation(Long id, InstallationRequest dto) {
        SoftwareInstallation installation=findInstallation(id); Device device=findDevice(dto.deviceId());
        Software product=findSoftware(dto.softwareId()); SoftwareLicense license=findOptionalLicense(dto.licenseId());
        validateInstallation(id,product,license,dto.status());
        installation.update(device,product,license,dto.installationDate(),dto.installedVersion(),dto.status());
        return installationResponse(saveInstallation(installation));
    }
    @Transactional public void deleteInstallation(Long id) { installations.delete(findInstallation(id)); }

    private void validateResponsible(ServiceUnit unit, ApplicationUser user) {
        if (!user.isActive()) throw new BusinessRuleException("Odgovorni korisnik mora biti aktivan.");
        if (!user.getServiceUnit().getId().equals(unit.getId())) throw new BusinessRuleException("Odgovorni korisnik mora pripadati istoj servisnoj jedinici kao uređaj.");
    }
    private void validateInstallation(Long currentId, Software product, SoftwareLicense license, InstallationStatus status) {
        if (license==null) return;
        if (!license.getSoftware().getId().equals(product.getId())) throw new BusinessRuleException("Izabrana licenca ne pripada instaliranom softveru.");
        if (status==InstallationStatus.INSTALLED) {
            long used=currentId==null?installations.countByLicenseIdAndStatus(license.getId(),InstallationStatus.INSTALLED)
                    :installations.countByLicenseIdAndStatusAndIdNot(license.getId(),InstallationStatus.INSTALLED,currentId);
            if (used>=license.getQuantity()) throw new BusinessRuleException("Iskorišćen je maksimalan broj instalacija za ovu licencu.");
        }
    }
    private void updateDevice(Device device,DeviceRequest dto,ServiceUnit unit,ApplicationUser responsible) {
        device.update(unit,responsible,dto.name().trim(),blankToNull(dto.hostname()),blankToNull(dto.ipAddress()),
                blankToNull(dto.serialNumber()),blankToNull(dto.manufacturer()),blankToNull(dto.model()),dto.deviceType(),dto.status());
    }
    private Device saveDevice(Device d) { try{return devices.saveAndFlush(d);}catch(DataIntegrityViolationException e){throw new BusinessRuleException("Hostname ili serijski broj već postoji.");} }
    private SoftwareInstallation saveInstallation(SoftwareInstallation i) { try{return installations.saveAndFlush(i);}catch(DataIntegrityViolationException e){throw new BusinessRuleException("Ovaj softver je već evidentiran na uređaju.");} }
    private String blankToNull(String value){return value==null||value.isBlank()?null:value.trim();}
    private Device findDevice(Long id){return devices.findById(id).orElseThrow(()->new ResourceNotFoundException("Uređaj",id));}
    private ServiceUnit findServiceUnit(Long id){return serviceUnits.findById(id).orElseThrow(()->new ResourceNotFoundException("Servisna jedinica",id));}
    private ApplicationUser findUser(Long id){return users.findById(id).orElseThrow(()->new ResourceNotFoundException("Korisnik",id));}
    private Software findSoftware(Long id){return software.findById(id).orElseThrow(()->new ResourceNotFoundException("Softver",id));}
    private SoftwareLicense findOptionalLicense(Long id){return id==null?null:licenses.findById(id).orElseThrow(()->new ResourceNotFoundException("Licenca",id));}
    private SoftwareInstallation findInstallation(Long id){return installations.findById(id).orElseThrow(()->new ResourceNotFoundException("Instalacija",id));}
    private DeviceResponse deviceResponse(Device d){ApplicationUser u=d.getResponsibleUser();return new DeviceResponse(d.getId(),d.getServiceUnit().getId(),d.getServiceUnit().getName(),u.getId(),u.getFirstName()+" "+u.getLastName(),d.getName(),d.getHostname(),d.getIpAddress(),d.getSerialNumber(),d.getManufacturer(),d.getModel(),d.getDeviceType(),d.getStatus(),d.getCreatedAt());}
    private InstallationResponse installationResponse(SoftwareInstallation i){return new InstallationResponse(i.getId(),i.getDevice().getId(),i.getDevice().getName(),i.getSoftware().getId(),i.getSoftware().getName(),i.getLicense()==null?null:i.getLicense().getId(),i.getInstallationDate(),i.getInstalledVersion(),i.getStatus());}
}
