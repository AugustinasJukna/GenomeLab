class Organism {
    constructor() {
        this.pos = createVector(random(width), random(height));
        this.size = 8;
        this.color = color(0, 130, 255);
        this.neuralNetwork = new NeuralNetwork(1, 5, 2);
        this.energy = 100;
        this.brainSize = 2;
        this.lifetime = 0;
        this.id = random(1000000);
        this.heading = random(-2 * PI, 2 * PI);
        this.velocity = 0;
        this.generation = 1;
    }

    display() {
        fill(this.color);
        noStroke();
        ellipse(this.pos.x, this.pos.y, this.size);

        // let directionLength = 15;
        // let directionX = this.pos.x + directionLength * cos(this.heading);
        // let directionY = this.pos.y + directionLength * sin(this.heading);

        // stroke(255, 0, 0);
        // line(this.pos.x, this.pos.y, directionX, directionY);
    }

    checkBorders() {
        if (this.pos.x < 0) {
            this.pos.x = 0;
        } else if (this.pos.x > width) {
            this.pos.x = width;
        }
        if (this.pos.y < 0) {
            this.pos.y = 0;
        } else if (this.pos.y > height) {
            this.pos.y = height;
        }
    }

    eat() {
        for (let i = food.length - 1; i >= 0; i--) {
            let d = Math.sqrt((this.pos.x - food[i].x) * (this.pos.x - food[i].x) + (this.pos.y - food[i].y) * (this.pos.y - food[i].y));
            if (d < this.size) {
                food.splice(i, 1);
                this.energy += 1;
            }
        }
    }

    nearestFood() {
        if (food.length === 0) return [0, 0, 0];
        let min_dist = Math.sqrt((this.pos.x - food[0].x) * (this.pos.x - food[0].x) + (this.pos.y - food[0].y) * (this.pos.y - food[0].y));
        let nearestFood = food[0];
        for (let i = 0; i < food.length; i++) {
            let d = Math.sqrt((this.pos.x - food[i].x) * (this.pos.x - food[i].x) + (this.pos.y - food[i].y) * (this.pos.y - food[i].y));
            if (d < min_dist) {
                min_dist = d;
                nearestFood = food[i];
            }
        }
        let normalizedX = nearestFood.x / width;
        let normalizedY = nearestFood.y / height;
        return [normalizedX, normalizedY, min_dist];
    }


    think() {
        let [nearestFoodX, nearestFoodY, min_dist] = this.nearestFood();
        let normalizedDist = min_dist / Math.sqrt(width * width + height * height);
        let relativeFoodX = (nearestFoodX - this.pos.x) / width;
        let relativeFoodY = (nearestFoodY - this.pos.y) / height;

        let rotationAngle = atan2(relativeFoodY, relativeFoodX) - atan2(0, 1);
        let normalizedRotation = (rotationAngle + PI) / (2 * PI);


        let theta = atan2(relativeFoodY, relativeFoodX);
        let normalizedTheta = (theta + PI) / (2 * PI);
        let normalizedX = this.pos.x / width;
        let normalizedY = this.pos.y / height;
        let input = [normalizedRotation];
        let [rotation, acceleration] = this.neuralNetwork.predict(input);
        let dt = 0.04;
        this.heading += rotation * dt;
        this.heading = (this.heading + 2 * PI) % (2 * PI);

        this.velocity += acceleration * dt;
        if (this.velocity > 2) this.velocity = 2;

        let dx = cos(this.heading) * this.velocity;
        let dy = sin(this.heading) * this.velocity;
        let move;
        move = createVector(dx, dy);
        this.pos.add(move);
        //this.energy -= 0.20;

        this.checkBorders();
        this.eat();
        let rotatedDirection = createVector(cos(this.heading), sin(this.heading));
        rotatedDirection.rotate(rotation * dt);
        let newDirectionX = this.pos.x + rotatedDirection.x * 10; // Adjust the length as needed
        let newDirectionY = this.pos.y + rotatedDirection.y * 10; // Adjust the length as needed
        stroke(255, 0, 0);
        line(this.pos.x, this.pos.y, newDirectionX, newDirectionY);
        this.heading = atan2(dy, dx);
    }

    /*     think() {
            let [nearestFoodX, nearestFoodY, min_dist] = this.nearestFood();
            let normalizedDist = min_dist / Math.sqrt(width * width + height * height);
            let relativeFoodX = (nearestFoodX - this.pos.x) / width;
            let relativeFoodY = (nearestFoodY - this.pos.y) / height;
    
            let rotationAngle = atan2(relativeFoodY, relativeFoodX) - atan2(0, 1);
            let normalizedRotation = (rotationAngle + PI) / (2 * PI);
    
            let input = [normalizedRotation, normalizedDist];
            let [rotation, acceleration] = this.neuralNetwork.predict(input);
            let dt = 0.02;
    
            this.heading += rotation * 720 * dt;
            this.heading = this.heading % 360;
    
            this.velocity += acceleration * 0.50;
            if (this.velocity > 1) this.velocity = 1;
    
    
            let dx = cos(this.heading) * this.velocity;
            let dy = sin(this.heading) * this.velocity;
            let move = createVector(dx, dy);
            this.pos.add(move);
    
            let directionLength = 10;
            let directionX = this.pos.x + directionLength * cos(this.heading);
            let directionY = this.pos.y + directionLength * sin(this.heading);
    
            stroke(255, 0, 0);
            line(this.pos.x, this.pos.y, directionX, directionY);
    
            this.checkBorders();
            this.eat();
            // let rotatedDirection = createVector(cos(this.heading), sin(this.heading));
            // rotatedDirection.rotate(rotation * rotationSpeed);
            // let newDirectionX = this.pos.x + rotatedDirection.x * 10;
            // let newDirectionY = this.pos.y + rotatedDirection.y * 10;
            // stroke(255, 0, 0);
            // line(this.pos.x, this.pos.y, newDirectionX, newDirectionY);
    
            this.age();
        } */


    age() {
        this.lifetime += 0.1;
    }

    copy() {
        let copy = new Organism();
        copy.neuralNetwork = this.neuralNetwork.copy();
        return copy;
    }

    crossover(partner) {
        let child = new Organism();
        child.neuralNetwork = this.neuralNetwork.crossover(partner.neuralNetwork);
        return child;
    }

    mutate(rate) {
        this.neuralNetwork.mutate(rate);
    }
}