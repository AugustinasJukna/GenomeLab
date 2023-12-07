package edu.ktu.GenomeLab.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read')")
    public String get() {
        return "GET ADMIN";
    }
    @PreAuthorize("hasAnyAuthority('admin:create')")
    @PostMapping
    public String post() {
        return "POST ADMIN";
    }
    @PreAuthorize("hasAnyAuthority('admin:update')")
    @PutMapping
    public String put() {
        return "PUT ADMIN";
    }
    @PreAuthorize("hasAnyAuthority('admin:delete')")
    @DeleteMapping
    public String delete() {
        return "DELETE ADMIN";
    }
}
