package com.example.dcim.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.*;

public class JwtRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
    public JwtRoleConverter() { authorities.setAuthoritiesClaimName("roles"); authorities.setAuthorityPrefix(""); }
    @Override public AbstractAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(jwt, authorities.convert(jwt), jwt.getSubject());
    }
}
