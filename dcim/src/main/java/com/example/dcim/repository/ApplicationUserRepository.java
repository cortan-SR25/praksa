package com.example.dcim.repository;

import com.example.dcim.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsernameIgnoreCase(String username);
    Optional<ApplicationUser> findByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    long countByActiveTrueAndRole(com.example.dcim.domain.UserRole role);
}
