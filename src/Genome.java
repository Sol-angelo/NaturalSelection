import java.util.Random;

class Genome {
    double[] genes;

    static Random rand = new Random();

    public Genome(int length) {
        genes = new double[length];
        for (int i = 0; i < length; i++) {
            genes[i] = rand.nextDouble() * 2 - 1; // [-1,1]
        }
    }

    // Single-point or uniform crossover
    public static Genome crossover(Genome g1, Genome g2) {
        Genome child = new Genome(g1.genes.length);
        for (int i = 0; i < g1.genes.length; i++) {
            child.genes[i] = rand.nextBoolean() ? g1.genes[i] : g2.genes[i];
        }
        return child;
    }

    // Mutate genome
    public void mutate(double mutationRate, double mutationSize) {
        for (int i = 0; i < genes.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                genes[i] += (rand.nextDouble() * 2 - 1) * mutationSize;
                genes[i] = Math.max(-1, Math.min(1, genes[i])); // clamp
            }
        }
    }
}