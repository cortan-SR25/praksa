package com.example.dcim.repository;
import com.example.dcim.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CompanyRepository extends JpaRepository<Company, Long> {}
