package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Organism;
import edu.ktu.GenomeLab.models.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Long> {

}