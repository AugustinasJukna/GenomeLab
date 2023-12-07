package edu.ktu.GenomeLab.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.ktu.GenomeLab.models.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    RESEARCHER_READ,
                    RESEARCHER_UPDATE,
                    RESEARCHER_DELETE,
                    RESEARCHER_CREATE
            )
    ),
    RESEARCHER(Set.of(RESEARCHER_READ, RESEARCHER_WRITE, RESEARCHER_UPDATE, RESEARCHER_DELETE, RESEARCHER_CREATE))
;

    @Getter
    private final Set<Permission>permissions;
    
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}