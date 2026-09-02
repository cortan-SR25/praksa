package com.example.dcim.service;

import com.example.dcim.api.dto.*;
import com.example.dcim.domain.*;
import com.example.dcim.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final ApplicationUserRepository users;
    private final ServiceUnitRepository serviceUnits;
    private final PasswordEncoder passwordEncoder;
    public UserService(ApplicationUserRepository users, ServiceUnitRepository serviceUnits, PasswordEncoder passwordEncoder) {
        this.users = users; this.serviceUnits = serviceUnits; this.passwordEncoder = passwordEncoder;
    }
    public List<UserResponse> list() { return users.findAll().stream().map(this::response).toList(); }
    public UserResponse get(Long id) { return response(find(id)); }
    @Transactional public UserResponse create(UserCreateRequest dto) {
        ensureUnique(null, dto.username(), dto.email());
        ApplicationUser user = new ApplicationUser(findServiceUnit(dto.serviceUnitId()), dto.username().trim(),
                passwordEncoder.encode(dto.password()), dto.firstName().trim(), dto.lastName().trim(),
                dto.email().trim(), dto.role());
        return response(users.save(user));
    }
    @Transactional public UserResponse update(Long id, UserUpdateRequest dto) {
        ApplicationUser user = find(id); ensureUnique(id, dto.username(), dto.email());
        if (user.getRole() == UserRole.ADMIN && user.isActive() && (dto.role() != UserRole.ADMIN || !dto.active())
                && users.countByActiveTrueAndRole(UserRole.ADMIN) <= 1) {
            throw new BusinessRuleException("Poslednji aktivni administrator ne može biti deaktiviran niti degradiran.");
        }
        user.update(findServiceUnit(dto.serviceUnitId()), dto.username().trim(), dto.firstName().trim(),
                dto.lastName().trim(), dto.email().trim(), dto.role(), dto.active());
        return response(user);
    }
    @Transactional public void changePassword(Long id, PasswordChangeRequest dto) { find(id).changePassword(passwordEncoder.encode(dto.password())); }
    @Transactional public void delete(Long id) {
        ApplicationUser user = find(id);
        if (user.getRole() == UserRole.ADMIN && user.isActive() && users.countByActiveTrueAndRole(UserRole.ADMIN) <= 1)
            throw new BusinessRuleException("Poslednji aktivni administrator ne može biti obrisan.");
        try { users.delete(user); users.flush(); }
        catch (DataIntegrityViolationException e) { throw new BusinessRuleException("Korisnik je povezan sa uređajima ili obnovama licence. Deaktivirajte ga umesto brisanja."); }
    }
    private void ensureUnique(Long currentId, String username, String email) {
        users.findByUsernameIgnoreCase(username.trim()).filter(u -> !u.getId().equals(currentId))
                .ifPresent(u -> { throw new BusinessRuleException("Korisničko ime već postoji."); });
        users.findByEmailIgnoreCase(email.trim()).filter(u -> !u.getId().equals(currentId))
                .ifPresent(u -> { throw new BusinessRuleException("Email već postoji."); });
    }
    private ApplicationUser find(Long id) { return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("Korisnik", id)); }
    private ServiceUnit findServiceUnit(Long id) { return serviceUnits.findById(id).orElseThrow(() -> new ResourceNotFoundException("Servisna jedinica", id)); }
    private UserResponse response(ApplicationUser u) { return new UserResponse(u.getId(), u.getServiceUnit().getId(), u.getServiceUnit().getName(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getRole(), u.isActive(), u.getCreatedAt()); }
}
