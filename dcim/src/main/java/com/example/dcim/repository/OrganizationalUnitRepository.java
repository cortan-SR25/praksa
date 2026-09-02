package com.example.dcim.repository;
import com.example.dcim.domain.OrganizationalUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrganizationalUnitRepository extends JpaRepository<OrganizationalUnit, Long> {
    List<OrganizationalUnit> findByCompanyIdOrderByName(Long companyId);
}
