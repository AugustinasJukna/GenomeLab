
let food = [];
let foodCount = 350;
let organisms = [];
let organismCount = 100;
let generationCount = 1;
let lastGeneration = 0;
let stepCounter = 0;
let fastForward = false;
const geneticAlgorithm = new GeneticAlgorithm(0.3);
const STEPS_PER_GENERATION = 300;



function setup() {
    createCanvas(800, 800);
    tf.setBackend('cpu');
    frameRate(30);
    for (let i = 0; i < organismCount; i++) {
        organisms.push(new Organism());
    }
}

function addFoodSquare() {
    food = [];
    let size = 200;
    // let startX = random(width);
    // let startY = random(height)
    let startX = 200;
    let startY = 400;
    for (let i = 0; i < size; i += 3) {
        for (let j = 0; j < size; j += 3) {
            let x = startX + i;
            let y = startY + j;
            food.push(createVector(x, y));
        }
    }
}

function die(i) {
    if (organisms[i] === undefined) {
        console.log("undefined")
        return true;

    }
    if (organisms[i].energy <= 0) {
        console.log("undefined")
        organisms.splice(i, 1);
        return true;
    }
    return false;
}




const ELITE_COUNT = 10;
function newGeneration() {
    for (let organism of organisms) {
        organism.fitness = calculateFitness(organism);
    }

    let newOrganisms = [];
    let sortedOrgs = [...organisms].sort((a, b) => b.fitness - a.fitness);
    newOrganisms.push(...sortedOrgs.slice(0, ELITE_COUNT));

    while (newOrganisms.length < organismCount) {
        let parent1 = tournamentSelection(organismCount / 5);
        console.log(parent1)
        let parent2 = tournamentSelection(organismCount / 5);
        let newOrganism = crossover(parent1, parent2);
        mutate(newOrganism);
        newOrganisms.push(newOrganism);
    }
    organisms = [newOrganisms];
}


function runGeneration() {
    food = [];
    addFoodSquare();
    for (let i = 0; i < STEPS_PER_GENERATION; i++) {
        for (let j = 0; j < organisms.length; j++) {
            if (die(j)) continue;
            organisms[j].think();
            organisms[j].display();
            organisms[j].age();
        }
    }
    newGeneration();

}

function fastForwardGenerations(numGenerations) {
    noLoop();
    for (let i = 0; i < numGenerations; i++) {
        runGeneration();
        console.log("Calculated generation: " + (i + 1));
    }
    loop();
}

function toggleFastForward() {
    fastForward = !fastForward;
}


function draw() {
    let slider = document.getElementById("myRange").value;
    background(0);
    stroke(0);
    strokeWeight(1);

    if (fastForward) {
        fastForwardGenerations(10);
        fastForward = !fastForward;
        generationCount += 10;
    } else {
        for (let j = 0; j < slider; j++) {
            background(0);
            stroke(0);
            strokeWeight(1);

            stroke(0, 255, 0);
            strokeWeight(2);
            if (Math.abs(lastGeneration - generationCount) > 0 || food.length === 0) {
                food = [];
                //addFoodSquare();
                for (let i = 0; i < foodCount; i++) {
                    let x = random(width);
                    let y = random(height);
                    food.push(createVector(x, y));
                }
            }
            lastGeneration = generationCount;

            for (let i = 0; i < organisms.length; i++) {
                if (die(j)) continue;
                organisms[i].think();
                organisms[i].display();
                organisms[i].age();
            }
            fill(255);
            textSize(16);
            text(`Generation: ${generationCount}`, 10, height - 20);
            stepCounter++;
            if (stepCounter >= STEPS_PER_GENERATION || organisms.length === 10) {
                console.log(organisms)
                organisms = geneticAlgorithm.newGeneration(organisms).slice();
                console.log(organisms[0].energy)
                stepCounter = 0;
                generationCount++;
            }
            stroke(0, 255, 0);
            strokeWeight(2);
            for (let i = 0; i < food.length; i++) {
                point(food[i].x, food[i].y);
            }
        }

    }



}



