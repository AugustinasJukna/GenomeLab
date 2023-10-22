package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Gene;
import edu.ktu.GenomeLab.repositories.GeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/genomes")
public class GeneController {
    @Autowired
    private GeneRepository geneRepository;

    @PostMapping("/create")
    public ResponseEntity<Gene>  addNewGene (@RequestParam String sequence) {
        return new ResponseEntity<>(geneRepository.save(new Gene(sequence)), HttpStatus.CREATED);
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<Gene>> getAllGenes()
    {
        List<Gene> genes = (List<Gene>) geneRepository.findAll();
        if (genes.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(genes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gene> getGeneById(@PathVariable Long id) {
        Gene gene = geneRepository.findById(id).orElse(null);
        if (gene == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gene);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Gene> updateGene(
            @PathVariable Long id,
            @RequestParam String sequence) {
        Gene gene = geneRepository.findById(id)
                .orElse(null);
        if (gene == null) return ResponseEntity.notFound().build();
        gene.setSequence(sequence);
        return new ResponseEntity<>(geneRepository.save(gene), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGene(@PathVariable Long id) {
        if (!geneRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        geneRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
