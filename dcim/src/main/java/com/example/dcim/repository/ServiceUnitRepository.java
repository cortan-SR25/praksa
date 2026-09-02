package com.example.dcim.repository;
import com.example.dcim.domain.ServiceUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceUnitRepository extends JpaRepository<ServiceUnit, Long> {
    List<ServiceUnit> findByOrganizationalUnitIdOrderByName(Long organizationalUnitId);
}
