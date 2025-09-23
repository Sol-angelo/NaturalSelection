import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class PredatorPopulation {
    List<Predator> predators = new ArrayList<>();
    Random rand = new Random();

    public PredatorPopulation(int size) {
        // Start with random traits
        for (int i = 0; i < size; i++) {
            predators.add(new Predator(115));
        }
    }
}