let food = [];
let FOOD_COUNT = 200;
let organisms = [];
let ORGANISMS_COUNT = 50;
let GENERATIONS_COUNT = 1;
let ELITE_COUNT = 10;
let MUTATION_RATE = 0.3;
let lastGeneration = 0;
let stepCounter = 0;
let fastForward = false;
const STEPS_PER_GENERATION = 500;
food = [];

async function fetchConstants() {
    try {
        const response = await fetch('./constants.json');
        const constants = await response.json();
        console.log('Constants fetched:', constants)
        return constants;
    } catch (error) {
        console.error('Error fetching constants:', error);
        throw error;
    }
}

let geneticAlgorithm;

async function setup() {
    createCanvas(800, 800);
    tf.setBackend('cpu');
    frameRate(30);
    let constants = await fetchConstants();
    ORGANISMS_COUNT = constants.ORGANISMS_COUNT;
    FOOD_COUNT = constants.FOOD_COUNT;
    ELITE_COUNT = constants.ELITE_COUNT;
    GENERATIONS_COUNT = constants.GENERATIONS_COUNT;
    MUTATION_RATE = constants.MUTATION_RATE;
    LOAD_FROM_FILE = constants.LOAD_FROM_FILE;
    for (let i = 0; i < constants.ORGANISMS_COUNT; i++) {
        organisms.push(new Organism());
    }
    geneticAlgorithm = new GeneticAlgorithm(MUTATION_RATE, ORGANISMS_COUNT, ELITE_COUNT);
    if (LOAD_FROM_FILE === 1) {
        await geneticAlgorithm.loadOrganismsWeightsFromFile(organisms);
    }
}

function addFoodSquare() {
    food = [];
    let size = 200;
    // let startX = random(width);
    // let startY = random(height)
    let startX = 200;
    let startY = 400;
    for (let i = 0; i < size; i += 4) {
        for (let j = 0; j < size; j += 4) {
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
    if (organisms[i].energy < 0) {
        organisms.splice(i, 1);
        return true;
    }
    return false;
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
    background(255);
    stroke(150);
    strokeWeight(1);

    if (fastForward) {
        fastForwardGenerations(10);
        fastForward = !fastForward;
        GENERATIONS_COUNT += 10;
    } else {
        for (let j = 0; j < slider; j++) {
            background(255);
            stroke(150);
            strokeWeight(1);

            stroke(0, 255, 0);
            strokeWeight(2);

            if (Math.abs(lastGeneration - GENERATIONS_COUNT) > 0 || food.length < FOOD_COUNT) {
                while (food.length < FOOD_COUNT) {
                    let x = random(width);
                    let y = random(height);
                    food.push(createVector(x, y));
                }
            }
            lastGeneration = GENERATIONS_COUNT;

            for (let i = 0; i < organisms.length; i++) {
                if (die(j)) continue;
                organisms[i].think();
                organisms[i].display();
                organisms[i].age();
            }

            fill(0);
            textSize(16);
            text(`Generation: ${GENERATIONS_COUNT}`, 10, height - 20);
            text(`Average Fitness: ${geneticAlgorithm.avgFitness}`, 10, height - 40);
            stepCounter++;

            if (stepCounter >= STEPS_PER_GENERATION) {
                organisms = geneticAlgorithm.newGeneration(organisms).slice();
                stepCounter = 0;
                GENERATIONS_COUNT++;
            }

            stroke(0, 255, 0);
            strokeWeight(6);
            for (let i = 0; i < food.length; i++) {
                point(food[i].x, food[i].y);
            }
        }
    }
}




