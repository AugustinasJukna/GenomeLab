package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Gene;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/genomes")
public class GenomeController {
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/create")
    public ResponseEntity<Gene>  addNewGenome (@RequestParam String sequence) {
        return new ResponseEntity<>(genomeRepository.save(new Gene(sequence)), HttpStatus.CREATED);
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<Gene>> getAllGenomes()
    {
        List<Gene> genes = (List<Gene>) genomeRepository.findAll();
        if (genes.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(genes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gene> getGenomeById(@PathVariable Long id) {
        Gene gene = genomeRepository.findById(id).orElse(null);
        if (gene == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gene);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Gene> updateGenome(
            @PathVariable Long id,
            @RequestParam String sequence) {
        Gene gene = genomeRepository.findById(id)
                .orElse(null);
        if (gene == null) return ResponseEntity.notFound().build();
        gene.setSequence(sequence);
        return new ResponseEntity<>(genomeRepository.save(gene), HttpStatus.OK);
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
