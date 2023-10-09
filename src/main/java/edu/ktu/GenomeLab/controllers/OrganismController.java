package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path="/organism")
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
    public Organism updateOrganism(
            @PathVariable Long id,
            @RequestBody Organism updatedOrganism) {
        return organismRepository.findById(id)
                .map(organism -> {
                    if (updatedOrganism.getGenome() != null) {
                        Genome genome = genomeRepository.findById(updatedOrganism.getGenome().getId())
                                .orElseThrow(() -> new NoSuchElementException("Genome not found"));
                        organism.setGenome(genome);
                    }

                    organism.setX(updatedOrganism.getX());
                    organism.setY(updatedOrganism.getY());
                    organism.setAge(updatedOrganism.getAge());

                    return organismRepository.save(organism);
                })
                .orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrganism(@PathVariable Long id) {
        if (!organismRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        organismRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
