package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Gene;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.repositories.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="/environments")
public class EnvironmentController {
    @Autowired
    private EnvironmentRepository environmentRepository;

    @PostMapping()
    public ResponseEntity<Environment> createEnvironment(
            @RequestBody Environment environment
            ) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<Environment>  updateEnvironment(
            @PathVariable Long id,
            @RequestBody Environment updatedEnvironment) {
        return environmentRepository.findById(id)
                .map(environment -> {
                    environment.setName(updatedEnvironment.getName());
                    environment.setDescription(updatedEnvironment.getDescription());
                    environment.setMutationCoefficient(updatedEnvironment.getMutationCoefficient());
                    return new ResponseEntity<Environment>(environmentRepository.save(environment), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable Long id) {
        if (!environmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        environmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/organisms")
    public ResponseEntity<List<Organism>> getAllOrganismsById(@PathVariable Long id) {
        Environment env =  environmentRepository.findById(id).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        List<Organism> organisms = env.getOrganisms();
        if (organisms.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organisms);
    }

    @GetMapping("/{idEnv}/organisms/{idOrg}")
    public ResponseEntity<List<Gene>> getAllGenomesByEnvironmentId(@PathVariable Long idEnv, @PathVariable Long idOrg) {
        Environment env =  environmentRepository.findById(idEnv).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        List<Organism> organisms = env.getOrganisms();
        Organism organism = null;
        for (Organism value : organisms) {
            if (Objects.equals(value.getId(), idOrg)) {
                organism = value;
            }
        }
        if (organism == null || organism.getGenes().isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(organism.getGenes());
    }
}
