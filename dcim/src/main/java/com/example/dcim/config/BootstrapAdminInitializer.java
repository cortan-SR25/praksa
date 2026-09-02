package com.example.dcim.config;

import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootstrapAdminInitializer implements ApplicationRunner {
    private final ApplicationUserRepository users; private final CompanyRepository companies;
    private final OrganizationalUnitRepository organizationalUnits; private final ServiceUnitRepository serviceUnits;
    private final PasswordEncoder passwordEncoder; private final String username; private final String password; private final String email;
    public BootstrapAdminInitializer(ApplicationUserRepository users, CompanyRepository companies,
            OrganizationalUnitRepository organizationalUnits, ServiceUnitRepository serviceUnits,
            PasswordEncoder passwordEncoder, @Value("${dcim.bootstrap-admin.username}") String username,
            @Value("${dcim.bootstrap-admin.password}") String password, @Value("${dcim.bootstrap-admin.email}") String email) {
        this.users=users; this.companies=companies; this.organizationalUnits=organizationalUnits; this.serviceUnits=serviceUnits;
        this.passwordEncoder=passwordEncoder; this.username=username; this.password=password; this.email=email;
    }
    @Override @Transactional public void run(ApplicationArguments args) {
        if (users.count() > 0) return;
        Company company = companies.save(new Company("DCIM Company"));
        OrganizationalUnit organization = organizationalUnits.save(new OrganizationalUnit(company, "IT"));
        ServiceUnit serviceUnit = serviceUnits.save(new ServiceUnit(organization, "Infrastructure"));
        users.save(new ApplicationUser(serviceUnit, username, passwordEncoder.encode(password), "System", "Administrator", email, UserRole.ADMIN));
    }
}
