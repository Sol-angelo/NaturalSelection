import java.util.Random;

class NeuralNetwork {
    int inputSize;
    int hiddenSize;
    int outputSize;

    double[][] weightsInputHidden; // [input][hidden]
    double[] biasHidden;           // [hidden]
    double[][] weightsHiddenOutput; // [hidden][output]
    double[] biasOutput;           // [output]

    static Random rand = new Random();

    // Standard constructor: random weights
    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;

        weightsInputHidden = new double[inputSize][hiddenSize];
        biasHidden = new double[hiddenSize];
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        biasOutput = new double[outputSize];

        randomizeWeights();
    }

    // Constructor using a flat genome array
    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize, double[] genomeWeights) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;

        weightsInputHidden = new double[inputSize][hiddenSize];
        biasHidden = new double[hiddenSize];
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        biasOutput = new double[outputSize];

        assignWeightsFromGenome(genomeWeights);
    }

    private void randomizeWeights() {
        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < hiddenSize; j++)
                weightsInputHidden[i][j] = rand.nextDouble() * 2 - 1;

        for (int j = 0; j < hiddenSize; j++)
            biasHidden[j] = rand.nextDouble() * 2 - 1;

        for (int j = 0; j < hiddenSize; j++)
            for (int k = 0; k < outputSize; k++)
                weightsHiddenOutput[j][k] = rand.nextDouble() * 2 - 1;

        for (int k = 0; k < outputSize; k++)
            biasOutput[k] = rand.nextDouble() * 2 - 1;
    }

    // Assign genome weights to network
    private void assignWeightsFromGenome(double[] genome) {
        int index = 0;

        // Input -> Hidden
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = genome[index++];
            }
        }

        // Hidden biases
        for (int j = 0; j < hiddenSize; j++) {
            biasHidden[j] = genome[index++];
        }

        // Hidden -> Output
        for (int j = 0; j < hiddenSize; j++) {
            for (int k = 0; k < outputSize; k++) {
                weightsHiddenOutput[j][k] = genome[index++];
            }
        }

        // Output biases
        for (int k = 0; k < outputSize; k++) {
            biasOutput[k] = genome[index++];
        }
    }

    // Feedforward pass
    public double[] feedForward(double[] inputs) {
        double[] hidden = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            double sum = biasHidden[j];
            for (int i = 0; i < inputSize; i++) {
                sum += inputs[i] * weightsInputHidden[i][j];
            }
            hidden[j] = Math.tanh(sum); // activation
        }

        double[] outputs = new double[outputSize];
        for (int k = 0; k < outputSize; k++) {
            double sum = biasOutput[k];
            for (int j = 0; j < hiddenSize; j++) {
                sum += hidden[j] * weightsHiddenOutput[j][k];
            }
            outputs[k] = Math.tanh(sum); // activation
        }

        return outputs;
    }

    // Crossover between two networks (produces a genome array)
    public static double[] crossover(NeuralNetwork n1, NeuralNetwork n2) {
        double[] genome = new double[n1.inputSize * n1.hiddenSize + n1.hiddenSize +
                n1.hiddenSize * n1.outputSize + n1.outputSize];
        Random rand = new Random();
        int index = 0;

        for (int i = 0; i < n1.inputSize; i++) {
            for (int j = 0; j < n1.hiddenSize; j++) {
                genome[index++] = rand.nextBoolean() ? n1.weightsInputHidden[i][j] : n2.weightsInputHidden[i][j];
            }
        }

        for (int j = 0; j < n1.hiddenSize; j++) {
            genome[index++] = rand.nextBoolean() ? n1.biasHidden[j] : n2.biasHidden[j];
        }

        for (int j = 0; j < n1.hiddenSize; j++) {
            for (int k = 0; k < n1.outputSize; k++) {
                genome[index++] = rand.nextBoolean() ? n1.weightsHiddenOutput[j][k] : n2.weightsHiddenOutput[j][k];
            }
        }

        for (int k = 0; k < n1.outputSize; k++) {
            genome[index++] = rand.nextBoolean() ? n1.biasOutput[k] : n2.biasOutput[k];
        }

        return genome;
    }

    // Produce a mutated copy of the network as a genome array
    public double[] mutateCopy(double mutationRate, double mutationSize) {
        double[] genome = flattenWeights();
        Random rand = new Random();
        for (int i = 0; i < genome.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                genome[i] += (rand.nextDouble() * 2 - 1) * mutationSize;
                genome[i] = Math.max(-1, Math.min(1, genome[i]));
            }
        }
        return genome;
    }

    // Flatten all weights and biases into a single array
    public double[] flattenWeights() {
        int length = inputSize * hiddenSize + hiddenSize + hiddenSize * outputSize + outputSize;
        double[] genome = new double[length];
        int index = 0;

        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < hiddenSize; j++)
                genome[index++] = weightsInputHidden[i][j];

        for (int j = 0; j < hiddenSize; j++)
            genome[index++] = biasHidden[j];

        for (int j = 0; j < hiddenSize; j++)
            for (int k = 0; k < outputSize; k++)
                genome[index++] = weightsHiddenOutput[j][k];

        for (int k = 0; k < outputSize; k++)
            genome[index++] = biasOutput[k];

        return genome;
    }
}