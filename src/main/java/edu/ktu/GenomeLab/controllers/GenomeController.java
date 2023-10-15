package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path="/genomes")
public class GenomeController {
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/add")
    public @ResponseBody Genome addNewGenome (@RequestParam String sequence) {
        return genomeRepository.save(new Genome(sequence));
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<Genome>> getAllGenomes()
    {
        List<Genome> genomes = (List<Genome>) genomeRepository.findAll();
        if (genomes.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(genomes);
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<Genome> getGenomeById(@PathVariable Long id) {
        return genomeRepository.findById(id);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Genome> updateGenome(
            @PathVariable Long id,
            @RequestParam String sequence) {
        Genome genome = genomeRepository.findById(id)
                .orElse(null);
        if (genome == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        genome.setSequence(sequence);
        return new ResponseEntity<Genome>(genomeRepository.save(genome), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGenome(@PathVariable Long id) {
        if (!genomeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        genomeRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
