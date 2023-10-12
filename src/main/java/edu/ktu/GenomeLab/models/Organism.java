package edu.ktu.GenomeLab.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "organisms")
public class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genome_id", referencedColumnName = "id")
    @JsonManagedReference
    private Genome genome;

    @Column(name = "x")
    private int x;
    @Column(name = "y")
    private int y;
    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    @JsonBackReference
    private Environment environment;

    public Organism() {

    }
    public Organism(Genome genome, int x, int y, int age) {
        this.genome = genome;
        this.x = x;
        this.y = y;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }






}
