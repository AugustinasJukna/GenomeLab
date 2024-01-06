package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.models.State;
import edu.ktu.GenomeLab.repositories.GeneRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/organisms")
public class OrganismController {
    @Autowired
    private OrganismRepository organismRepository;

    @PostMapping("/")
    public ResponseEntity<Organism> addNewOrganism (@RequestBody Organism organism) {
        return new ResponseEntity<>(organismRepository.save(organism), HttpStatus.CREATED);
    }

    @PostMapping("/addBatch")
    public ResponseEntity<String> addOrganisms(@RequestBody List<Organism> organisms) {
        try {
            organismRepository.saveAll(organisms);
            return new ResponseEntity<>("Organisms added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding organisms: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        organism.setEnergy(updatedOrganism.getEnergy());
        organism.setGenes(updatedOrganism.getGenes());
        organism.setX(updatedOrganism.getX());
        organism.setY(updatedOrganism.getY());
        Organism updatedEntity = organismRepository.save(organism);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganism(@PathVariable Long id) {
        if (!organismRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organismRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getByState/{stateId}")
    public ResponseEntity<List<Organism>> getOrganismsByStateId(@PathVariable Long stateId) {
        List<Organism> organisms = organismRepository.findByStateId(stateId);
        return ResponseEntity.ok(organisms);
    }

    @DeleteMapping("/deleteBatch")
    public ResponseEntity<Void> deleteOrganismsInBatch(@RequestBody List<Long> organismIds) {
        try {
            organismRepository.deleteOrganismsByIds(organismIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
