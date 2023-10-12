package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.User;
import edu.ktu.GenomeLab.repositories.EnvironmentRepository;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/environments")
public class EnvironmentController {
    @Autowired
    private EnvironmentRepository environmentRepository;
    @Autowired
    private OrganismRepository organismRepository;
    @Autowired
    private GenomeRepository genomeRepository;

    @PostMapping("/create")
    public Environment createEnvironment(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double mutationCoefficient) {
        Environment environment = new Environment(name, description);
        environment.setMutationCoefficient(mutationCoefficient);
        return environmentRepository.save(environment);
    }

    @GetMapping
    public Iterable<Environment> getAllEnvironments() {
        return  environmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Environment> getEnvironmentById(@PathVariable Long id) {
        return environmentRepository.findById(id);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Environment>  updateEnvironment(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double mutationCoefficient) {
        return environmentRepository.findById(id)
                .map(environment -> {
                    environment.setName(name);
                    environment.setDescription(description);
                    environment.setMutationCoefficient(mutationCoefficient);
                    return new ResponseEntity<Environment>(environmentRepository.save(environment), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteEnvironment(@PathVariable Long id) {
        if (!environmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        environmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllGenomesByEnvironmentId/{id}")
    public Iterable<Genome> getAllGenomesByEnvironmentId(@PathVariable Long id) {
        return environmentRepository.getAllGenomesByEnvironmentId(id);
    }
}
