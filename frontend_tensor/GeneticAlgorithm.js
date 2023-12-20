class GeneticAlgorithm {
    constructor(mutationRate) {
        this.mutationRate = mutationRate;
        this.topFitnessScore = 0;
        this.populationSize = 50;
        this.bestFitnessHistory = [];
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
        for (let o of organisms) {
            o.fitness = o.energy + o.lifetime;
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
                this.bestFitnessHistory.push(this.topFitnessScore);
            }

        }
        console.log("Top fitness: " + this.topFitnessScore);
        return totalFitness;
    }

    newGeneration(organisms) {
        let totalFitness = this.calculatePopulationFitness(organisms);
        let newGeneration = [];

        let eliteCount = Math.round(this.populationSize * 0.1);
        let sortedOrganisms = organisms.slice().sort((a, b) => b.fitness - a.fitness);
        let elites = sortedOrganisms.slice(0, eliteCount);
        newGeneration.push(...elites);


        while (newGeneration.length < this.populationSize - eliteCount) {
            let parentA = this.rouletteWheelSelection(organisms, totalFitness);
            let parentB = this.rouletteWheelSelection(organisms, totalFitness);
            console.log('1 ' + parentA)
            console.log('2 ' + parentB)

            let child = parentA.crossover(parentB);
            child.mutate(this.mutationRate);
            newGeneration.push(child);
        }
        while (newGeneration.length < this.populationSize) {
            newGeneration.push(new Organism())
        }
        for (let i = 0; i < organisms.length; i++) {
            console.log(organisms[i])
            tf.tidy(() => {
                if (organisms[i].neuralNetwork) {
                    organisms[i].neuralNetwork.dispose();
                }
            });
        }

        return newGeneration;
    }
}
