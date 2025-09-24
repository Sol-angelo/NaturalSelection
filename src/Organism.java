import java.util.*;
import java.util.stream.IntStream;

class Organism {
    Genome genome;           // holds all traits and NN weights
    NeuralNetwork brain;     // decoded from genome
    double x, y;
    int foodEaten = 0;
    double fitness = 0;
    int energy;
    int age = 0;
    int maturityAge = 25;
    int colony;
    double speed;
    double aggression;

    static Random rand = new Random();

    // World dimensions
    static final double WORLD_WIDTH = 800;
    static final double WORLD_HEIGHT = 600;

    // Constructor for new random organism
    public Organism(int genomeLength, int colony) {
        this.genome = new Genome(genomeLength);
        this.colony = colony;
        decodeGenome();
        energy = 200 + rand.nextInt(200);

        if (colony == 0) {
            x = rand.nextDouble() * WORLD_WIDTH / 2;
            y = rand.nextDouble() * WORLD_HEIGHT;
        } else {
            x = rand.nextDouble() * WORLD_WIDTH / 2 + WORLD_WIDTH / 2;
            y = rand.nextDouble() * WORLD_HEIGHT;
        }
    }

    // Constructor from genome (used in reproduction)
    public Organism(Genome genome, int colony) {
        this.genome = genome;
        this.colony = colony;
        decodeGenome();
        energy = 200 + rand.nextInt(200);

            x = rand.nextDouble() * WORLD_WIDTH;
            y = rand.nextDouble() * WORLD_HEIGHT;
    }

    // Decode genome into traits and neural network
    public void decodeGenome() {
        // Example mapping: genome[0] = speed, genome[1] = aggression, rest = NN weights
        speed = 0.1 + Math.max(0, genome.genes[0] * 5);                 // scaled 0.1–5
        aggression = (genome.genes[1] + 1) / 2.0;                       // normalize to 0–1

        // remaining genes for NN weights
        int nnWeights = genome.genes.length - 2;
        double[] nnArray = Arrays.copyOfRange(genome.genes, 2, genome.genes.length);
        brain = new NeuralNetwork(7, 10, 3, nnArray); // assume constructor can accept weights array
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void move(List<Food> food, List<Organism> allies, List<Organism> opponents) {
        double[] foodVector = vectorToNearestFood(food);
        double[] allyVector = vectorToNearestOrganism(allies);
        double[] opponentVector = vectorToNearestOrganism(opponents);

        age++;
        energy -= speed / 3;

        double[] inputs = new double[]{
                foodVector[0], foodVector[1],
                allyVector[0], allyVector[1],
                opponentVector[0], opponentVector[1],
                energy / 400
        };

        double[] outputs = brain.feedForward(inputs);

        double dx = outputs[0];
        double dy = outputs[1];

        // normalize direction so they don't "prefer" diagonal
        double mag = Math.sqrt(dx*dx + dy*dy);
        if (mag > 0) {
            dx /= mag;
            dy /= mag;
        }

        x += dx * speed * 1.5;
        y += dy * speed * 1.5;

        aggression = outputs[2];
        if (aggression > 0.5 && opponentVector != null) {
            x += opponentVector[0] * speed * 0.5;
            y += opponentVector[1] * speed * 0.5;
        } else if (aggression <= 0.5 && opponentVector != null) {
            x -= opponentVector[0] * speed * 0.5;
            y -= opponentVector[1] * speed * 0.5;
        }

        // stay in bounds
        x = Math.max(0, Math.min(WORLD_WIDTH, x));
        y = Math.max(0, Math.min(WORLD_HEIGHT, y));
    }

    public boolean canReproduce() {
        return age >= maturityAge && Math.random() < 0.01;
    }

    public Organism selectMate(List<Organism> population) {
        return population.get(rand.nextInt(population.size()));
    }

    // Reproduction: crossover genomes
    public static Organism reproduce(Organism parent1, Organism parent2) {
        Genome childGenome = Genome.crossover(parent1.genome, parent2.genome);
        childGenome.mutate(0.01, 0.2); // 1% mutation rate, +/- 0.2
        return new Organism(childGenome, parent1.colony);
    }

    public void eatFood(List<Food> foodList) {
        Iterator<Food> it = foodList.iterator();
        while (it.hasNext()) {
            Food f = it.next();
            double dx = x - f.x;
            double dy = y - f.y;
            if (dx * dx + dy * dy < 100) {
                foodEaten++;
                energy += 5;
                it.remove();
                break;
            }
        }
    }

    // Find nearest ally
    public Organism nearestAlly(List<Organism> colony) {
        return colony.stream()
                .filter(o -> o != this)
                .min(Comparator.comparingDouble(o -> distanceSquared(o.x, o.y)))
                .orElse(null);
    }

    // Find nearest opponent
    public Organism nearestOpponent(List<Organism> otherColony) {
        return otherColony.stream()
                .min(Comparator.comparingDouble(o -> distanceSquared(o.x, o.y)))
                .orElse(null);
    }

    public double distanceSquared(double x1, double y1) {
        double dx = x - x1;
        double dy = y - y1;
        return dx * dx + dy * dy;
    }

    // Helper: vector to nearest organism
    public double[] vectorToNearestOrganism(List<Organism> list) {
        if (list.isEmpty()) return new double[]{0, 0};
        Organism nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Organism o : list) {
            if (o == this) continue;
            double dist = distanceSquared(o.x, o.y);
            if (dist < minDist) {
                minDist = dist;
                nearest = o;
            }
        }
        if (nearest == null) return new double[]{0, 0};
        double dx = (nearest.x - x) / WORLD_WIDTH;
        double dy = (nearest.y - y) / WORLD_HEIGHT;
        return new double[]{dx, dy};
    }

    // Helper: vector to nearest food
    public double[] vectorToNearestFood(List<Food> foods) {
        if (foods.isEmpty()) return new double[]{0, 0};
        Food nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Food f : foods) {
            double dist = distanceSquared(f.x, f.y);
            if (dist < minDist) {
                minDist = dist;
                nearest = f;
            }
        }
        double dist = Math.sqrt(minDist);
        if (nearest == null) return new double[]{0, 0};
        double dx = (nearest.x - x) / dist; // direction unit vector
        double dy = (nearest.y - y) / dist;
        return new double[]{dx, dy};
    }
}