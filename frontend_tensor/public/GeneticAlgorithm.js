class GeneticAlgorithm {
    constructor(mutationRate) {
        this.mutationRate = mutationRate;
        this.topFitnessScore = 0;
        this.populationSize = 50;
        this.bestFitnessHistory = [];
        this.avgFitness = 0;
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

    sample(arr, count) {
        const shuffled = arr.slice().sort(() => Math.random() - 0.5);
        return shuffled.slice(0, count);
    }

    truncationSelection(organisms, elitismNum) {
        const orgsSorted = organisms.slice().sort((a, b) => b.fitness - a.fitness);

        const candidates = Array.from({ length: elitismNum }, (_, index) => index);

        const randomIndices = this.sample(candidates, 2);
        const org1 = orgsSorted[randomIndices[0]];
        const org2 = orgsSorted[randomIndices[1]];

        return [org1, org2];
    }

    calculateFitness(organisms) {
        for (let o of organisms) {
            o.fitness = o.energy;
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
        console.log("Avg fitness: " + this.topFitnessScore / population.length);
        this.avgFitness = this.topFitnessScore / population.length;
        return totalFitness;
    }

    async saveOrganismsWeightsToFile(organisms) {
        let organismsData = [];

        for (let organism of organisms) {
            if (organism.neuralNetwork) {
                const genes = organism.neuralNetwork.model.getWeights();
                const organismData = {
                    x: organism.pos.x,
                    y: organism.pos.y,
                    energy: organism.energy,
                    genes: genes.map(w => w.arraySync()),
                };
                organismsData.push(organismData);
            }
        }

        const jsonContent = JSON.stringify(organismsData, null, 2);

        try {
            const response = await fetch('http://localhost:3500/writeJson', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ jsonData: jsonContent }),
            });

            const result = await response.json();
            console.log('Organisms data saved successfully:', result);
        } catch (error) {
            console.error('Error saving organisms data:', error);
        }
    }




    newGeneration(organisms) {
        this.saveOrganismsWeightsToFile(organisms);
        let totalFitness = this.calculatePopulationFitness(organisms);
        let newGeneration = [];

        let eliteCount = Math.round(this.populationSize * 0.1);
        let sortedOrganisms = organisms.slice().sort((a, b) => b.fitness - a.fitness);
        let elites = sortedOrganisms.slice(0, eliteCount);
        newGeneration.push(...elites);


        while (newGeneration.length < this.populationSize - eliteCount) {
            // let parentA = this.rouletteWheelSelection(organisms, totalFitness);
            // let parentB = this.rouletteWheelSelection(organisms, totalFitness);
            const [parentA, parentB] = this.truncationSelection(organisms, eliteCount);
            let child = parentA.crossover(parentB);
            child.mutate(this.mutationRate);
            child.energy = 0;
            child.fitness = 0;
            newGeneration.push(child);
        }
        while (newGeneration.length < this.populationSize) {
            newGeneration.push(new Organism())
        }
        // for (let i = 0; i < organisms.length; i++) {
        //     console.log(organisms[i])
        //     tf.tidy(() => {
        //         if (organisms[i].neuralNetwork) {
        //             organisms[i].neuralNetwork.dispose();
        //         }
        //     });
        // }
        // for (let i = 0; i < organisms.length; i++) {
        //     organisms[i].neuralNetwork.dispose();
        // }
        return newGeneration;
    }


}

