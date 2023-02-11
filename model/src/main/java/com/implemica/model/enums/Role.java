package com.implemica.model.enums;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enum of application user roles.
 */
@AllArgsConstructor
public enum Role {
    ADMIN(Set.of(Permission.CREATE,Permission.READ,Permission.UPDATE,Permission.DELETE));

    private final Set<Permission> permissions;

    /**
     * The method returns the set of {@link SimpleGrantedAuthority} for the role in the application.
     *
     * @return set of {@link SimpleGrantedAuthority}.
     * @see SimpleGrantedAuthority
     */
    public Set<SimpleGrantedAuthority> getAuthorities(){
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.permission))
                .collect(Collectors.toSet());
    }
}
