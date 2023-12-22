package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.GeneRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/organisms")
public class OrganismController {
    @Autowired
    private OrganismRepository organismRepository;
    @Autowired
    private GeneRepository geneRepository;

    @PostMapping("/")
    public ResponseEntity<Organism> addNewOrganism (@RequestBody Organism organism) {
        return new ResponseEntity<>(organismRepository.save(organism), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Organism>> getAllOrganisms()
    {
        List<Organism> organisms = (List<Organism>) organismRepository.findAll();
        if (organisms.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organisms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organism> getOrganismById(@PathVariable Long id) {
        Organism organism = organismRepository.findById(id).orElse(null);
        if (organism == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organism);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Organism> updateOrganism(
            @PathVariable Long id,
            @RequestBody Organism updatedOrganism) {
        Organism organism = organismRepository.findById(id)
                .orElse(null);;
        if (organism == null) return ResponseEntity.notFound().build();
        organism.setX(updatedOrganism.getX());
        organism.setY(updatedOrganism.getY());
        organism.setAge(updatedOrganism.getAge());
        return ResponseEntity.ok(organism);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganism(@PathVariable Long id) {
        if (!organismRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organismRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
