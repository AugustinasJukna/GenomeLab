package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path="/organisms")
public class OrganismController {
    @Autowired
    private OrganismRepository organismRepository;
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/add")
    public @ResponseBody Organism addNewOrganism (@RequestParam Long genomeId, @RequestParam int x, @RequestParam int y, @RequestParam int age) {
        Genome genome = genomeRepository.findById(genomeId).orElseThrow(() -> new NoSuchElementException("Genome not found"));;
        return organismRepository.save(new Organism(genome, x, y, age));
    }

    @GetMapping("/")
    public @ResponseBody Iterable< Organism> getAllGenomes() {
        return organismRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<Organism> getGenomeById(@PathVariable Long id) {
        return organismRepository.findById(id);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Organism> updateOrganism(
            @PathVariable Long id,
            @RequestParam Long genomeId,
            @RequestParam int x,
            @RequestParam int y,
            @RequestParam int age) {
        Genome genome = genomeRepository.findById(genomeId)
                .orElse(null);
        Organism organism = organismRepository.findById(id)
                .orElse(null);;
        if (genome == null || organism == null) return ResponseEntity.notFound().build();
        organism.setGenome(genome);
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
