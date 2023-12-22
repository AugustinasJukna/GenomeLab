package edu.ktu.GenomeLab.repositories;

import edu.ktu.GenomeLab.models.Organism;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganismRepository extends CrudRepository<Organism, Long> {
    @Query("SELECT o FROM Organism o WHERE o.state.id = :stateId")
    List<Organism> findByStateId(@Param("stateId") Long stateId);

    @Query("DELETE FROM Organism o WHERE o.id IN :organismIds")
    void deleteOrganismsByIds(@Param("organismIds") List<Long> organismIds);
}
