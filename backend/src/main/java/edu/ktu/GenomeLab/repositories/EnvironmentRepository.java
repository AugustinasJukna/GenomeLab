package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Environment;
import edu.ktu.GenomeLab.models.Gene;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnvironmentRepository extends CrudRepository<Environment, Long> {
    List<Environment> findByUserId(Long userId);
}
