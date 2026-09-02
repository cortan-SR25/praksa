package com.example.dcim.repository;
import com.example.dcim.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByServiceUnitIdOrderByName(Long serviceUnitId);
    List<Device> findByResponsibleUserIdOrderByName(Long responsibleUserId);
}
