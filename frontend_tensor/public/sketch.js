let food = [];
let FOOD_COUNT = 200;
let organisms = [];
let ORGANISMS_COUNT = 50;
let GENERATIONS_COUNT = 1;
let ELITE_COUNT = 10;
let lastGeneration = 0;
let stepCounter = 0;
let fastForward = false;
const geneticAlgorithm = new GeneticAlgorithm(0.3);
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



async function setup() {
    createCanvas(800, 800);
    tf.setBackend('cpu');
    frameRate(30);
    for (let i = 0; i < ORGANISMS_COUNT; i++) {
        organisms.push(new Organism());
    }

    let constants = await fetchConstants();
    ORGANISMS_COUNT = constants.ORGANISMS_COUNT;
    FOOD_COUNT = constants.FOOD_COUNT;
    ELITE_COUNT = constants.ELITE_COUNT;
    GENERATIONS_COUNT = constants.GENERATIONS_COUNT;
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
    background(255); // Set background color to white
    stroke(150); // Set grid color
    strokeWeight(1); // Set grid line thickness

    // Draw grid
    for (let i = 20; i < width; i += 20) {
        line(i, 0, i, height);
    }
    for (let j = 20; j < height; j += 20) {
        line(0, j, width, j);
    }

    if (fastForward) {
        fastForwardGenerations(10);
        fastForward = !fastForward;
        GENERATIONS_COUNT += 10;
    } else {
        for (let j = 0; j < slider; j++) {
            background(255); // Set background color to white
            stroke(150); // Set grid color
            strokeWeight(1); // Set grid line thickness

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

            fill(0); // Set text color to black
            textSize(16);
            text(`Generation: ${GENERATIONS_COUNT}`, 10, height - 20);
            text(`Average Fitness: ${geneticAlgorithm.avgFitness}`, 10, height - 40);
            stepCounter++;

            if (stepCounter >= STEPS_PER_GENERATION || organisms.length === 10) {
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




