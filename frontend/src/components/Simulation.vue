<!-- Simulation.vue -->

<template>
  <div>
    <h1>Genetic Algorithm Simulation</h1>
    <button @click="toggleFastForward">Toggle Fast Forward</button>
    <input type="range" min="1" max="50" value="1" class="slider" id="myRange" />
    <div ref="p5Canvas"></div>
  </div>
</template>

<script>
import p5 from "p5";
import * as tf from "@tensorflow/tfjs";
import GeneticAlgorithm from "../../simulation/GeneticAlgorithm";
import Organism from "../../simulation/Organism";



export default {
  mounted() {
    this.setupP5();
  },
  methods: {
    setupP5() {
      this.sketch = new p5(this.p5Sketch, this.$refs.p5Canvas);
    },
    toggleFastForward() {
      this.sketch.toggleFastForward();
    },
    p5Sketch(p) {
      let food = [];
      let foodCount = 200;
      let organisms = [];
      let organismCount = 10;
      let generationCount = 1;
      let lastGeneration = 0;
      let stepCounter = 0;
      let fastForward = false;
      const geneticAlgorithm = new GeneticAlgorithm(0.3);
      const STEPS_PER_GENERATION = 500;
      food = [];

      p.setup = () => {
        p.createCanvas(800, 800);
        tf.setBackend("cpu");
        p.frameRate(30);
        for (let i = 0; i < organismCount; i++) {
          organisms.push(new Organism());
        }
      };

      p.addFoodSquare = () => {
        food = [];
        let size = 200;
        let startX = 200;
        let startY = 400;
        for (let i = 0; i < size; i += 4) {
          for (let j = 0; j < size; j += 4) {
            let x = startX + i;
            let y = startY + j;
            food.push(p.createVector(x, y));
          }
        }
      };

      p.die = (i) => {
        if (organisms[i] === undefined) {
          console.log("undefined");
          return true;
        }
        if (organisms[i].energy < 0) {
          organisms.splice(i, 1);
          return true;
        }
        return false;
      };

      const ELITE_COUNT = 10;
      p.newGeneration = () => {
        for (let organism of organisms) {
          organism.fitness = p.calculateFitness(organism);
        }

        let newOrganisms = [];
        let sortedOrgs = [...organisms].sort((a, b) => b.fitness - a.fitness);
        newOrganisms.push(...sortedOrgs.slice(0, ELITE_COUNT));

        while (newOrganisms.length < organismCount) {
          let parent1 = p.tournamentSelection(organismCount / 5);
          let parent2 = p.tournamentSelection(organismCount / 5);
          let newOrganism = p.crossover(parent1, parent2);
          p.mutate(newOrganism);
          newOrganisms.push(newOrganism);
        }
        organisms = [...newOrganisms];
      };

      p.runGeneration = () => {
        food = [];
        p.addFoodSquare();
        for (let i = 0; i < STEPS_PER_GENERATION; i++) {
          for (let j = 0; j < organisms.length; j++) {
            if (p.die(j)) continue;
            organisms[j].think();
            organisms[j].display(p);
            organisms[j].age();
          }
        }
        p.newGeneration();
      };

      p.fastForwardGenerations = (numGenerations) => {
        p.noLoop();
        for (let i = 0; i < numGenerations; i++) {
          p.runGeneration();
          console.log("Calculated generation: " + (i + 1));
        }
        p.loop();
      };

      p.toggleFastForward = () => {
        fastForward = !fastForward;
      };

      p.draw = () => {
        let slider = document.getElementById("myRange").value;
        p.background(0);
        p.stroke(0);
        p.strokeWeight(1);

        if (fastForward) {
          p.fastForwardGenerations(10);
          fastForward = !fastForward;
          generationCount += 10;
        } else {
          for (let j = 0; j < slider; j++) {
            p.background(0);
            p.stroke(0);
            p.strokeWeight(1);

            p.stroke(0, 255, 0);
            p.strokeWeight(2);
            if (
                Math.abs(lastGeneration - generationCount) > 0 ||
                food.length < foodCount
            ) {
              while (food.length < foodCount) {
                let x = p.random(p.width);
                let y = p.random(p.height);
                food.push(p.createVector(x, y));
              }
            }
            lastGeneration = generationCount;

            for (let i = 0; i < organisms.length; i++) {
              if (p.die(j)) continue;
              organisms[i].think();
              organisms[i].display(p);
              organisms[i].age();
            }
            p.fill(255);
            p.textSize(16);
            p.text(`Generation: ${generationCount}`, 10, p.height - 20);
            stepCounter++;
            if (
                stepCounter >= STEPS_PER_GENERATION ||
                organisms.length === 10
            ) {
              organisms = geneticAlgorithm.newGeneration(organisms).slice();
              stepCounter = 0;
              generationCount++;
            }
            p.stroke(0, 255, 0);
            p.strokeWeight(6);
            for (let i = 0; i < food.length; i++) {
              p.point(food[i].x, food[i].y);
            }
          }
        }
      };
    },
  },
};
</script>

<style scoped>
/* Your styles here */
</style>
