package edu.ktu.GenomeLab.controllers;
import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.models.State;
import edu.ktu.GenomeLab.repositories.OrganismRepository;
import edu.ktu.GenomeLab.repositories.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/states")
public class StateController {
    @Autowired
    private StateRepository stateRepository;

    @PostMapping()
    public ResponseEntity<State> addNewState (@RequestBody State state) {
        return new ResponseEntity<>(stateRepository.save(state), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<State>> getAllStates()
    {
        List<State> states = (List<State>) stateRepository.findAll();
        if (states.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Long id) {
        State state = stateRepository.findById(id).orElse(null);
        if (state == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(state);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<State> updateState(
            @PathVariable Long id,
            @RequestBody State updatedState) {
        State state = stateRepository.findById(id)
                .orElse(null);;
        if (state == null) return ResponseEntity.notFound().build();
        state.setDate(updatedState.getDate());
        state.setOrganisms(updatedState.getOrganisms());
        return ResponseEntity.ok(state);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        if (!stateRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        stateRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
