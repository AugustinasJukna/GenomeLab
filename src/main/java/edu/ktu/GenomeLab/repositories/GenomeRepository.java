package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Genome;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GenomeRepository extends CrudRepository<Genome, Long>{
}

