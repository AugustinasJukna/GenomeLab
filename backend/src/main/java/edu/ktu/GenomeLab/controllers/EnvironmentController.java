package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Gene;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.models.State;
import edu.ktu.GenomeLab.repositories.EnvironmentRepository;
import edu.ktu.GenomeLab.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="api/v1/environments")
public class EnvironmentController {
    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private StateRepository stateRepository;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Environment>> getEnvironmentsByUserId(@PathVariable Long userId) {
        List<Environment> environments = environmentRepository.findByUserId(userId);
        return ResponseEntity.ok(environments);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Environment>  updateEnvironment(
            @PathVariable Long id,
            @RequestBody Environment updatedEnvironment) {
        return environmentRepository.findById(id)
                .map(environment -> {
                    environment.setName(updatedEnvironment.getName() == null ? environment.getName() : updatedEnvironment.getName());
                    environment.setDescription(updatedEnvironment.getDescription() == null ? environment.getDescription() : updatedEnvironment.getDescription());
                    environment.setMutationCoefficient(updatedEnvironment.getMutationCoefficient() == 0 ? environment.getMutationCoefficient() : updatedEnvironment.getMutationCoefficient());
                    environment.setFoodCount(updatedEnvironment.getFoodCount() == 0 ? environment.getFoodCount() : updatedEnvironment.getFoodCount());
                    environment.setOrganismsCount(updatedEnvironment.getOrganismsCount() == 0 ? environment.getOrganismsCount() : updatedEnvironment.getOrganismsCount());
                    environment.setEliteCount(updatedEnvironment.getEliteCount() == 0 ? environment.getEliteCount() : updatedEnvironment.getEliteCount());
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

    @GetMapping("/{id}/states")
    public ResponseEntity<List<State>> getAllStatesById(@PathVariable Long id) {
        Environment env =  environmentRepository.findById(id).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        List<State> states = env.getStates();
        if (states.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{idEnv}/states/{idState}")
    public ResponseEntity<State> getStatesByEnvIdAndOrgId(@PathVariable Long idEnv, @PathVariable Long idState) {
        Environment env =  environmentRepository.findById(idEnv).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        List<State> states = env.getStates();
        State state = null;
        for (State value : states) {
            if (Objects.equals(value.getId(), idState)) {
                state = value;
            }
        }
        if (state == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(state);
    }

    @GetMapping("/{idEnv}/states/{idState}/organisms")
    public ResponseEntity<List<Organism>> getAllOrganismsByEnvironmentId(@PathVariable Long idEnv, @PathVariable Long idState) {
        Environment env =  environmentRepository.findById(idEnv).orElse(null);
        if (env == null) return ResponseEntity.notFound().build();
        List<State> states = env.getStates();
        State state = null;
        for (State value : states) {
            if (Objects.equals(value.getId(), idState)) {
                state = value;
            }
        }
        if (state == null || state.getOrganisms().isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(state.getOrganisms());
    }


}

