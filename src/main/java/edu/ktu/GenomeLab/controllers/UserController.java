package edu.ktu.GenomeLab.controllers;

import edu.ktu.GenomeLab.models.Genome;
import edu.ktu.GenomeLab.models.User;
import edu.ktu.GenomeLab.models.enums.Role;
import edu.ktu.GenomeLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestParam String name, @RequestParam String password, @RequestParam String email, @RequestParam Role role) {
        User user = new User(name, password, email, role);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        if (users.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/genome")
    public ResponseEntity<List<Object[]>> getMostCommonGenomeInAllEnvironments() {
        List<Object[]> genome =  userRepository.getMostCommonGenomeInAllEnvironments();
        if (genome.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(genome);
    }


}

