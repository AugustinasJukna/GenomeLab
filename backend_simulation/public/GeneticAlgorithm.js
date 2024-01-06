class GeneticAlgorithm {
    constructor(mutationRate, populationSize, elitism) {
        this.mutationRate = mutationRate;
        this.topFitnessScore = 0;
        this.populationSize = populationSize;
        this.bestFitnessHistory = [];
        this.avgFitness = 0;
        this.elitism = elitism;
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

    truncationSelection(orgsSorted, elitismNum) {
        //const orgsSorted = organisms.slice().sort((a, b) => b.fitness - a.fitness);

        if (elitismNum === 0) {
            return orgsSorted.slice(0, 2);
        }

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
                const selectedGenes = genes;//genes.slice(2, 4);

                const organismData = {
                    x: organism.pos.x,
                    y: organism.pos.y,
                    energy: organism.energy,
                    genes: selectedGenes.map(w => w.arraySync()),
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

    async loadOrganismsWeightsFromFile(organisms) {
        try {
            const response = await fetch('http://localhost:3500/readJson', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const data = await response.json();
            const result = JSON.parse(data.jsonData);
            console.log('Organisms data loaded successfully:', result);
            console.log(organisms.length)
            for (let i = 0; i < organisms.length; i++) {
                console.log("Loading organism " + result[i]);
                // const genes = result[i].genes.map(w => tf.tensor(w));
                // organisms[i].neuralNetwork.model.setWeights(genes);
                organisms[i].pos.x = result[i].x;
                organisms[i].pos.y = result[i].y;
                organisms[i].energy = result[i].energy;
            }
            return organisms;
        } catch (error) {
            console.error('Error loading organisms data:', error);
        }
    }




    newGeneration(organisms) {
        this.saveOrganismsWeightsToFile(organisms);
        let totalFitness = this.calculatePopulationFitness(organisms);
        let newGeneration = [];

        let sortedOrganisms = organisms.slice().sort((a, b) => b.fitness - a.fitness);
        let elites = sortedOrganisms.slice(0, this.elitism);
        newGeneration.push(...elites);


        while (newGeneration.length < this.populationSize) {
            // let parentA = this.rouletteWheelSelection(organisms, totalFitness);
            // let parentB = this.rouletteWheelSelection(organisms, totalFitness);
            const [parentA, parentB] = this.truncationSelection(sortedOrganisms, this.elitism);
            let child = parentA.crossover(parentB);
            child.mutate(this.mutationRate);
            child.energy = 0;
            child.fitness = 0;
            newGeneration.push(child);
        }
        console.log("New generation: ");
        console.log(newGeneration)
        console.log("New generation length: " + newGeneration.length)
        // while (newGeneration.length < this.populationSize) {
        //     newGeneration.push(new Organism())
        // }
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
        for (let i = 0; i < organisms.length; i++) {
            organisms[i].generation++;
        }
        return newGeneration;
    }


}

