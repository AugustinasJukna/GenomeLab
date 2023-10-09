package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Genome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EnvironmentRepository extends CrudRepository<Environment, Long> {
    @Query("SELECT g FROM Genome g " +
            "INNER JOIN Organism o ON g.id = o.genome.id " +
            "INNER JOIN Environment e ON o.environment.id = e.id " +
            "WHERE e.id = :environmentId")
    Iterable<Genome> getAllGenomesByEnvironmentId(@Param("environmentId") Long environmentId);
}
