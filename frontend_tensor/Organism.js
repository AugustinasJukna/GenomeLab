class Organism {
    constructor() {
        this.pos = createVector(random(width), random(height));
        this.size = 4;
        this.color = color(255, 255, 255);
        this.neuralNetwork = new NeuralNetwork(4, 4, 2);
        this.energy = 100;
        this.lifetime = 0;
    }

    display() {
        fill(this.color);
        noStroke();
        ellipse(this.pos.x, this.pos.y, this.size);
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
            if (d < this.size / 2) {
                food.splice(i, 1);
                this.energy += 10;
                this.size += 1;
            }
        }
    }

    nearestFood() {
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
        let theta = atan2(relativeFoodY, relativeFoodX);
        let normalizedTheta = (theta + PI) / (2 * PI);
        let normalizedX = this.pos.x / width;
        let normalizedY = this.pos.y / height;
        let input = [relativeFoodX, relativeFoodY, normalizedDist, normalizedTheta];
        let [dx, dy] = this.neuralNetwork.predict(input);
        let move;
        move = createVector(dx, dy);
        this.pos.add(move);
        this.energy -= 0.35;
        this.checkBorders();
        this.eat();
        // if (eatSignal > doNotEatSignal) {
        //     this.eat();
        // }

    }

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