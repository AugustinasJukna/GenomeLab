package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.User;
import edu.ktu.GenomeLab.repositories.EnvironmentRepository;
import edu.ktu.GenomeLab.repositories.GenomeRepository;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import org.apache.coyote.Response;
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

    @PostMapping("/create")
    public ResponseEntity<Environment> createEnvironment(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double mutationCoefficient) {
        Environment environment = new Environment(name, description);
        environment.setMutationCoefficient(mutationCoefficient);
        return new ResponseEntity<>(environmentRepository.save(environment), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Environment>> getAllEnvironments()
    {
        List<Environment> environments = (List<Environment>) environmentRepository.findAll();
        if (environments.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(environments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable Long id) {
        Environment env = environmentRepository.findById(id).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(env);
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
    public ResponseEntity<Void> deleteEnvironment(@PathVariable Long id) {
        if (!environmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        environmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/genomes/{id}")
    public ResponseEntity<List<Genome>> getAllGenomesByEnvironmentId(@PathVariable Long id) {
        List<Genome> genomes = (List<Genome>) environmentRepository.getAllGenomesByEnvironmentId(id);
        if (genomes.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(genomes);
    }
}
