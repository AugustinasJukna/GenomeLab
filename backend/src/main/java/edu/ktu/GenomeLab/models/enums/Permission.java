package edu.ktu.GenomeLab.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public enum Permission {
    RESEARCHER_READ("researcher:read"),
    RESEARCHER_UPDATE("researcher:update"),
    RESEARCHER_DELETE("researcher:delete"),
    RESEARCHER_CREATE("researcher:create"),
    RESEARCHER_WRITE("researcher:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:create");

@Getter
    private final String permission;


}
