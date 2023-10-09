package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/genome")
public class GenomeController {
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/add")
    public @ResponseBody Genome addNewGenome (@RequestParam String sequence) {
        return genomeRepository.save(new Genome(sequence));
    }

    @GetMapping
    public @ResponseBody Iterable< Genome> getAllGenomes() {
        return genomeRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<Genome> getGenomeById(@PathVariable Long id) {
        return genomeRepository.findById(id);
    }

    @PatchMapping("/update/{id}")
    public Genome updateGenome(
            @PathVariable Long id,
            @RequestParam String sequence) {
        return genomeRepository.findById(id)
                .map(genome -> {
                    genome.setSequence(sequence);
                    return genomeRepository.save(genome);
                })
                .orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGenome(@PathVariable Long id) {
        if (!genomeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        genomeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
