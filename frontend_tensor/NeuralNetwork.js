class NeuralNetwork {
    constructor(a, b, c, d) {
        if (a instanceof tf.Sequential) {
            this.model = a;
            this.input_nodes = b;
            this.hidden_nodes = c;
            this.output_nodes = d;

        } else {
            this.input_nodes = a;
            this.hidden_nodes = b;
            this.output_nodes = c;
            this.model = this.createModel();
        }
    }

    predict(array) {
        return tf.tidy(() => {
            const X = tf.tensor2d([array]);
            const Y = this.model.predict(X);
            const outputs = Y.dataSync();
            return outputs;
        });
    }

    dispose() {
        this.model.dispose();
    }

    createModel() {
        const model = tf.sequential();
        const hidden = tf.layers.dense({
            units: this.hidden_nodes,
            inputShape: [this.input_nodes],
            activation: 'relu'
        });
        model.add(hidden);
        let activations = ['sigmoid', 'relu', 'tanh'];

        const output = tf.layers.dense({
            units: this.output_nodes,
            activation: 'sigmoid'
        });
        model.add(output);
        return model;
    }

    copy() {
        return tf.tidy(() => {
            const copy = this.createModel();
            const weights = this.model.getWeights();
            const weightCopies = [];
            for (let i = 0; i < weights.length; i++) {
                weightCopies[i] = weights[i].clone();
            }
            copy.setWeights(weights);
            return new NeuralNetwork(copy, this.input_nodes, this.hidden_nodes, this.output_nodes);
        });
    }

    crossover(partner) {
        return tf.tidy(() => {
            const child = this.createModel();
            const parentAWeights = this.model.getWeights();
            const parentBWeights = partner.model.getWeights();
            const childWeights = [];
            for (let i = 0; i < parentAWeights.length; i++) {
                let tensorA = parentAWeights[i];
                let tensorB = parentBWeights[i];
                let shape = parentAWeights[i].shape;
                let valuesA = tensorA.dataSync().slice();
                let valuesB = tensorB.dataSync().slice();
                let values = [];
                for (let j = 0; j < valuesA.length; j++) {
                    if (random(1) < 0.5) {
                        values.push(valuesA[j]);
                    } else {
                        values.push(valuesB[j]);
                    }
                }
                let newTensor = tf.tensor(values, shape);
                childWeights.push(newTensor);
            }
            //child.setWeights(childWeights);
            if (random(1) < 0.5) {
                child.setWeights(parentAWeights);
            } else {
                child.setWeights(parentBWeights);
            }
            return new NeuralNetwork(child, this.input_nodes, this.hidden_nodes, this.output_nodes);
        });
    }

    mutate(rate) {
        tf.tidy(() => {
            const weights = this.model.getWeights();
            const mutatedWeights = [];
            for (let i = 0; i < weights.length; i++) {
                let tensor = weights[i];
                let shape = weights[i].shape;
                let values = tensor.dataSync().slice();
                for (let j = 0; j < values.length; j++) {
                    if (random(1) < rate) {
                        let rnd = randomGaussian();
                        while (rnd === 0){
                            rnd = randomGaussian();
                        }
                        values[j] += rnd;
                    }
                }
                let newTensor = tf.tensor(values, shape);
                mutatedWeights[i] = newTensor;
            }
            this.model.setWeights(mutatedWeights);
            console.log("mutated")
        });
    }
}
