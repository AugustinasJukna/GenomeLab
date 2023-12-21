package edu.ktu.GenomeLab.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "environment")
    @JsonManagedReference
    @JsonIgnore
    private List<State> states;
    @Column(name = "mutationCoefficient")
    private double mutationCoefficient;

    @Column(name = "organismsCount")
    private int organismsCount;

    public int getOrganismsCount() {
        return organismsCount;
    }

    public void setOrganismsCount(int organismsCount) {
        this.organismsCount = organismsCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public int getEliteCount() {
        return eliteCount;
    }

    public void setEliteCount(int eliteCount) {
        this.eliteCount = eliteCount;
    }

    @Column(name = "foodCount")
    private int foodCount;

    @Column(name = "eliteCount")
    private int eliteCount;

    public Environment(String name, String description, double mutationCoefficient, int organismsCount, int foodCount, int eliteCount) {
        this.name = name;
        this.description = description;
        this.mutationCoefficient = mutationCoefficient;
        this.organismsCount = organismsCount;
        this.foodCount = foodCount;
        this.eliteCount = eliteCount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public double getMutationCoefficient() {
        return mutationCoefficient;
    }

    public void setMutationCoefficient(double mutationCoefficient) {
        this.mutationCoefficient = mutationCoefficient;
    }


    @Override
    public String toString() {
        return "Environment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
