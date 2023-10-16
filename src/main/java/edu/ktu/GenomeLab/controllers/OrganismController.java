package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import edu.ktu.GenomeLab.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path="/organisms")
public class OrganismController {
    @Autowired
    private OrganismRepository organismRepository;
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/create")
    public ResponseEntity<Organism> addNewOrganism (@RequestParam int x, @RequestParam int y, @RequestParam int age) {
        //Genome genome = genomeRepository.findById(genomeId).orElse(null);
        Genome genome = genomeRepository.save(new Genome(Utils.generateRandomSequence(16)));
        //if (genome == null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(organismRepository.save(new Organism(genome, x, y, age)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Organism>> getAllGenomes()
    {
        List<Organism> organisms = (List<Organism>) organismRepository.findAll();
        if (organisms.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organisms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organism> getGenomeById(@PathVariable Long id) {
        Organism organism = organismRepository.findById(id).orElse(null);
        if (organism == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organism);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Organism> updateOrganism(
            @PathVariable Long id,
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam int age) {
        Organism organism = organismRepository.findById(id)
                .orElse(null);;
        if (organism == null) return ResponseEntity.notFound().build();
        organism.setX(x);
        organism.setY(y);
        organism.setAge(age);
        return ResponseEntity.ok(organism);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrganism(@PathVariable Long id) {
        if (!organismRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organismRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
