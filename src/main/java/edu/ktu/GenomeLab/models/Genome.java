package edu.ktu.GenomeLab.models;

import jakarta.persistence.*;

@Entity
@Table(name = "genomes")
public class Genome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence")
    private String sequence;

    public Genome() {}

    public Genome(String sequence) {
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






}
