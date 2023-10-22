package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Gene;
import org.springframework.data.repository.CrudRepository;

public interface GeneRepository extends CrudRepository<Gene, Long>{
}

