package edu.ktu.GenomeLab.repositories;
import edu.ktu.GenomeLab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User, Long> {
    @Query(value = "SELECT joined.sequence, COUNT(*) AS sequence_count " +
            "FROM (SELECT e.id AS environment_id, g.sequence " +
            "FROM environment e " +
            "JOIN organisms o ON e.id = o.environment_id " +
            "JOIN genomes g ON o.genome_id = g.id) AS joined " +
            "GROUP BY joined.sequence " +
            "ORDER BY sequence_count DESC " +
            "LIMIT 1", nativeQuery = true)
    List<Object[]> getMostCommonGenomeInAllEnvironments();

    Optional<User> findByEmail(String email);
}
