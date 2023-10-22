package edu.ktu.GenomeLab.controllers;


import edu.ktu.GenomeLab.models.Gene;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import edu.ktu.GenomeLab.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/organisms")
public class OrganismController {
    @Autowired
    private OrganismRepository organismRepository;
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/")
    public ResponseEntity<Organism> addNewOrganism (@RequestBody Organism organism) {
        //Genome genome = genomeRepository.findById(genomeId).orElse(null);
        Gene gene1 = genomeRepository.save(new Gene(Utils.generateRandomSequence(16)));
        Gene gene2 = genomeRepository.save(new Gene(Utils.generateRandomSequence(16)));
        List<Gene> genes = new ArrayList<>();
        genes.add(0, gene1);
        genes.add(2, gene2);

        organism.setGenomes(genes);
        //if (genome == null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(organismRepository.save(organism), HttpStatus.CREATED);
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
            @RequestBody Organism updatedOrganism) {
        Organism organism = organismRepository.findById(id)
                .orElse(null);;
        if (organism == null) return ResponseEntity.notFound().build();
        organism.setX(updatedOrganism.getX());
        organism.setY(updatedOrganism.getY());
        organism.setAge(updatedOrganism.getAge());
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
