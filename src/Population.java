import java.util.*;

class Population {
    List<Organism> organisms = new ArrayList<>();
    Random rand = new Random();

    public Population(int size) {
        // Start with random traits
        for (int i = 0; i < size; i++) {
            organisms.add(new Organism(115));
        }
    }

    // Select based on fitness (roulette wheel selection)
    private Organism selectParent() {
        double totalFitness = organisms.stream().mapToDouble(o -> o.fitness).sum();
        double pick = rand.nextDouble() * totalFitness;
        double cumulative = 0;
        for (Organism o : organisms) {
            cumulative += o.fitness;
            if (cumulative >= pick) return o;
        }
        return organisms.get(rand.nextInt(organisms.size())); // fallback
    }

    // Create next generation
    public void evolve() {
        List<Organism> newGen = new ArrayList<>();
        for (int i = 0; i < organisms.size(); i++) {
            Organism parent = selectParent();
            //newGen.add(parent.reproduce());
        }
        organisms = newGen;
    }

    public Organism getBest() {
        return organisms.stream().max(Comparator.comparingDouble(o -> o.fitness)).orElse(null);
    }
}