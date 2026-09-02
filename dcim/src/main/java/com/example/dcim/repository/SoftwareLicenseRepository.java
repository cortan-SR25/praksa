package com.example.dcim.repository;

import com.example.dcim.domain.SoftwareLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;

public interface SoftwareLicenseRepository extends JpaRepository<SoftwareLicense, Long> {
    List<SoftwareLicense> findByEndDateBetweenOrderByEndDateAsc(LocalDate from, LocalDate to);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from SoftwareLicense l where l.id = :id")
    Optional<SoftwareLicense> findByIdForUpdate(Long id);
}
