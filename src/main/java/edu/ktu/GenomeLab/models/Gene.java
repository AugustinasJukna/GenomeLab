package edu.ktu.GenomeLab.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "genes")
public class Gene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sequence;

    public Gene() {}

    public Gene(String sequence) {
        this.sequence = sequence;
    }

    public Long getId() {
        return id;
    }


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @ManyToOne
    @JoinColumn(name = "organism_id")
    @JsonBackReference
    @JsonIgnore
    private Organism organism;

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }






}
