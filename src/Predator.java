import java.util.*;

class Predator {
    Genome genome;           // holds traits and neural network weights
    NeuralNetwork brain;     // decoded from genome
    double x, y;
    int energyGained = 0;
    double fitness = 0;
    int energy;
    int age;
    double speed;
    double aggression;
    static final double WORLD_WIDTH = Main.worldWidth;
    static final double WORLD_HEIGHT = Main.worldHeight;
    static Random rand = new Random();
    int visionRadius = 500;

    // Constructor for a random predator
    public Predator(int genomeLength) {
        genome = new Genome(genomeLength);
        decodeGenome();
        energy = 100 + rand.nextInt(200);
        age = 0;
        x = rand.nextDouble() * WORLD_WIDTH;
        y = rand.nextDouble() * WORLD_HEIGHT;
    }

    // Constructor from a genome (used in reproduction)
    public Predator(Genome genome) {
        this.genome = genome;
        decodeGenome();
        energy = 100 + rand.nextInt(200);
        age = 0;
        x = rand.nextDouble() * WORLD_WIDTH;
        y = rand.nextDouble() * WORLD_HEIGHT;
    }

    // Decode genome into traits and neural network
    public void decodeGenome() {
        // genome[0] = speed, genome[1] = aggression
        speed = Math.max(0.1 + genome.genes[0] * 5, 1);
        aggression = (genome.genes[1] + 1) / 2.0;                  // normalize 0–1

        // remaining genes for NN weights
        int nnLength = genome.genes.length - 2;
        double[] nnArray = Arrays.copyOfRange(genome.genes, 2, genome.genes.length);
        brain = new NeuralNetwork(7, 10, 3, nnArray); // example NN with same inputs/outputs as organisms
    }

    public boolean isDead() {
        return energy <= 0;
    }

    // Move toward nearest prey (either colony)
    public void move(List<Organism> colony1) {
        Organism nearest = nearestPrey(colony1);
        if (nearest == null) return;

        // Neural network inputs: vector to nearest prey, own energy, maybe other inputs
        double dx = (nearest.x - x) / WORLD_WIDTH;
        double dy = (nearest.y - y) / WORLD_HEIGHT;
        double[] inputs = new double[]{dx, dy, energy / 400, 0, 0, 0, 0}; // fill remaining 7 inputs

        double[] outputs = brain.feedForward(inputs);

        x += outputs[0] * speed;
        y += outputs[1] * speed;

        aggression = outputs[2];
        if (aggression > 0.5) { // optional: charge more aggressively toward prey
            x += dx * speed * 2;
            y += dy * speed * 2;
        } else { // more cautious
            x += dx * speed * 1.5;
            y += dy * speed * 1.5;
        }

        // stay inside world
        x = Math.max(0, Math.min(WORLD_WIDTH, x));
        y = Math.max(0, Math.min(WORLD_HEIGHT, y));

        age++;
        energy -= speed / 2.5;

        // Eat prey if close
        double distSq = distanceSquared(nearest);
        if (distSq < 100) {
            energy += nearest.energy/20;
            energyGained += nearest.energy/20;// predator gains energy
            nearest.energy = 0; // prey dies
        }
    }

    private Organism nearestPrey(List<Organism> colony1) {
        List<Organism> allPrey = new ArrayList<>();
        allPrey.addAll(colony1);

        return allPrey.stream()
                .filter(o -> o.energy > 0)
                .min(Comparator.comparingDouble(this::distanceSquared))
                .orElse(null);
    }

    private double distanceSquared(Organism o) {
        double dx = o.x - x;
        double dy = o.y - y;
        return dx * dx + dy * dy;
    }

    // Reproduction: crossover genomes
    public static Predator reproduce(Predator p1, Predator p2) {
        Genome childGenome = Genome.crossover(p1.genome, p2.genome);
        childGenome.mutate(0.01, 0.2);
        return new Predator(childGenome);
    }

    public boolean canReproduce() {
        return age >= 40 && Math.random() < 0.5;
    }

    public Predator selectMate(List<Predator> population) {
        Predator mate;
        ArrayList<Predator> available = selectPredatorsInRange(population, this, visionRadius);
        if (available.size() > 0) {
            for(int i = 0; i < available.size(); i++) {
                System.out.println("flag");
                mate = available.get(rand.nextInt(available.size()));
                double dist = distanceSquared(mate.x, mate.y);
                if (dist <= visionRadius && mate.canReproduce()) {
                    return mate;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public ArrayList<Predator> selectPredatorsInRange(List<Predator> population, Predator initial, int radius) {
        ArrayList<Predator> organisms = new ArrayList<>();
        for (Predator o : population) {
            if (o != initial) {
                if (distanceSquared(o.x, o.y) <= radius) {
                    organisms.add(o);
                }
            }
        }
        if (organisms.size() > 0) {
            System.out.println("predators: " + organisms);
        }
        return organisms;
    }

    public double distanceSquared(double x1, double y1) {
        double dx = x - x1;
        double dy = y - y1;
        return dx * dx + dy * dy;
    }
}