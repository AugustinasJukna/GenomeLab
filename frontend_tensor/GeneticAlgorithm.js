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
        console.log("old " + organisms.length)
        while (newGeneration.length < this.populationSize) {
            let parentA = this.rouletteWheelSelection(organisms, totalFitness);
            let parentB = this.rouletteWheelSelection(organisms, totalFitness);
            let count = 0;
            while (parseFloat(parentB.id) === parseFloat(parentA.id)) {
                if (count > 1000) break;
                parentB = this.rouletteWheelSelection(organisms, totalFitness);
                count++;
            }
            let child = parentA.crossover(parentB);
            child.mutate(this.mutationRate);
            newGeneration.push(child);
        }
        for (let i = 0; i < organisms.length; i++) {
            organisms[i].neuralNetwork.dispose();
        }
        console.log("new " + newGeneration.length)
        return newGeneration;
    }
}
