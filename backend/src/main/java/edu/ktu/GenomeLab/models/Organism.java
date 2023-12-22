package edu.ktu.GenomeLab.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "organisms")
public class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genes")
    private String genes;

    @Column(name = "x")
    private int x;
    @Column(name = "y")
    private int y;

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Column(name = "energy")
    private int energy;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @JsonBackReference
    private State state;

    public Organism() {

    }
    public Organism(int x, int y, int energy, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.energy = energy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenes() {
        return genes;
    }

    public void setGenes(String genes) {
        this.genes = genes;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }






}
