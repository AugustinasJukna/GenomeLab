class GeneticAlgorithm {
    constructor(mutationRate) {
        this.mutationRate = mutationRate;
        this.topFitnessScore = 0;
        this.populationSize = 100;
    }

    rouletteWheelSelection(population, tfit) {
        let rand = random(0, tfit);
        let sum = 0;
        for (let i = 0; i < population.length; i++) {
            sum += population[i].fitness;
            if (sum >= rand) {
                return population[i];
            }
        }
    }

    calculateFitness(organisms) {
        let sum = 0;
        for (let o of organisms) {
            sum += o.energy;
        }
        for (let o of organisms) {
            o.fitness = o.energy / sum;
        }
    }


    calculatePopulationFitness(population) {
        let totalFitness = 0;
        let bestOfGeneration = population[0];
        this.calculateFitness(population);
        for (let organism of population) {
            totalFitness += organism.fitness;
            if (organism.fitness > bestOfGeneration.fitness) {
                bestOfGeneration = organism;
            }
            if (bestOfGeneration.fitness > this.topFitnessScore) {
                this.topFitnessScore = bestOfGeneration.fitness;
            }

        }
        console.log("Top fitness: " + this.topFitnessScore);
        return totalFitness;
    }

    newGeneration(organisms) {
        let totalFitness = this.calculatePopulationFitness(organisms);
        let newGeneration = [];
        while (newGeneration.length < this.populationSize - this.populationSize * 0.1) {
            let parentA = this.rouletteWheelSelection(organisms, totalFitness);
            //let parentB = this.rouletteWheelSelection(organisms, totalFitness);
            //console.log(parentA)
            //console.log(parentB)

            let child = parentA.crossover(parentA);
            child.mutate(this.mutationRate);
            newGeneration.push(child);
        }
        while (newGeneration.length < this.populationSize) {
            newGeneration.push(new Organism())
        }
        for (let i = 0; i < organisms.length; i++) {
            organisms[i].neuralNetwork.dispose();
        }
        return newGeneration;
    }
}
