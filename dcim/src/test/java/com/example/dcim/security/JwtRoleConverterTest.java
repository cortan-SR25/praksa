package com.example.dcim.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class JwtRoleConverterTest {
    @Test
    void convertsRolesClaimToSpringAuthorities() {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60),
                java.util.Map.of("alg", "HS256"), java.util.Map.of("sub", "admin", "roles", List.of("ROLE_ADMIN")));
        var authentication = new JwtRoleConverter().convert(jwt);
        assertThat(authentication.getName()).isEqualTo("admin");
        assertThat(authentication.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
    }
}
