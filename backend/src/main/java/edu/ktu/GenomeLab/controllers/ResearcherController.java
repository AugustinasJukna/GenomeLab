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
@RequestMapping("/api/v1/research")
public class ResearcherController {
    @GetMapping
    public String get() {
        return "GET RESEARCHER";
    }
    @PostMapping
    public String post() {
        return "POST RESEARCHER";
    }
    @PutMapping
    public String put() {
        return "PUT RESEARCHER";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE RESEARCHER";
    }
}
