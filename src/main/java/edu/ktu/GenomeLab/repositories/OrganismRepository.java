package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Organism;
import org.springframework.data.repository.CrudRepository;

public interface OrganismRepository extends CrudRepository<Organism, Long> {
}
