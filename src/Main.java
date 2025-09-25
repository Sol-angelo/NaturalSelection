import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Population popa = new Population(450);
        PredatorPopulation predators = new PredatorPopulation(65);
        List<Food> food = new ArrayList<>();
        for (int i = 0; i < 500; i++) food.add(new Food());

        JFrame frame = new JFrame("Natural Selection Simulation");
        PopulationPanel popPanel = new PopulationPanel(popa.organisms, predators.predators, food);
        GraphPanel graphPanel = new GraphPanel();
        StatsPanel statsPanel = new StatsPanel();

        frame.setLayout(new BorderLayout());
        frame.add(popPanel, BorderLayout.CENTER);
        frame.add(graphPanel, BorderLayout.SOUTH);
        frame.add(statsPanel, BorderLayout.EAST);
        graphPanel.setPreferredSize(new Dimension(800, 300));
        statsPanel.setPreferredSize(new Dimension(300, 800));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final int[] steps = {0};
        final int generationLength = 3;

        Timer timer = new Timer(2, e -> {
            List<Organism> newBabiesa = new ArrayList<>();
            List<Predator> newBabiesp = new ArrayList<>();
            // Move + eat
            for (Organism o : popa.organisms) {
                o.move(food, popa.organisms, predators.predators);
                o.eatFood(food);
                if (o.canReproduce(popa.organisms)) {
                    // choose a mate
                    Organism mate = o.selectMate(popa.organisms);
                    newBabiesa.add(Organism.reproduce(o, mate));
                }
                o.energy -= 2;
            }

            if (!popa.organisms.isEmpty()) {
                if (popa.organisms.size() < 50 && Math.random() < 0.1) {
                    popa.organisms.add(new Organism(new Genome(popa.organisms.getFirst().genome.genes.length)));
                }
            }
            if (!predators.predators.isEmpty()) {
                if (predators.predators.size() < 25 && Math.random() < 0.1) {
                    predators.predators.add(new Predator(new Genome(predators.predators.getFirst().genome.genes.length)));
                }
            }
            for (Predator p : predators.predators) {
                p.move(popa.organisms);
                if (p.canReproduce()) {
                    // choose a mate
                    Predator mate = p.selectMate(predators.predators);
                    newBabiesp.add(Predator.reproduce(p, mate));
                }
                p.energy -= 2;
            }
            int maxFood = 1000;
            List<Food> newFood = new ArrayList<>();
            for (Food f : food) {
                if (Math.random() < 0.02 && food.size() + newFood.size() < maxFood) { // 2% chance to reproduce per step
                    newFood.add(f.reproduce(800, 600));
                    newFood.add(f.reproduce(800, 600));
                }
            }
            food.addAll(newFood);

            if (food.size() < maxFood) {
                if (Math.random() < 0.5) {
                    food.add(new Food());
                    food.add(new Food());// 10% chance each tick
                }
            }

            // remove dead organisms
            popa.organisms.removeIf(Organism::isDead);
            predators.predators.removeIf(Predator::isDead);
            // add new babies
            popa.organisms.addAll(newBabiesa);
            predators.predators.addAll(newBabiesp);

            food.removeIf(Food::isDead);

            steps[0]++;
            if (steps[0] >= generationLength) {
                steps[0] = 0;
                for (Organism o : popa.organisms) {
                    o.fitness += o.foodEaten*10;
                    o.foodEaten = 0;
                }
                for (Predator p : predators.predators) {
                    p.fitness += p.energyGained*10;
                    p.energyGained = 0;
                }

                double avgFitnessa = popa.organisms.stream().mapToDouble(o -> o.fitness).average().orElse(0);
                double avgEnergya = popa.organisms.stream().mapToDouble(o -> o.energy).average().orElse(0);
                double avgSpeeda  = popa.organisms.stream().mapToDouble(o -> o.speed).average().orElse(0);
                double avgAggressiona  = popa.organisms.stream().mapToDouble(o -> o.aggression).average().orElse(0);
                double avgFitnessp = predators.predators.stream().mapToDouble(o -> o.fitness).average().orElse(0);
                double avgEnergyp = predators.predators.stream().mapToDouble(o -> o.energy).average().orElse(0);
                double avgSpeedp  = predators.predators.stream().mapToDouble(o -> o.speed).average().orElse(0);
                double avgAggressionp  = predators.predators.stream().mapToDouble(o -> o.aggression).average().orElse(0);
                graphPanel.addData("fitnessa", avgFitnessa);
                graphPanel.addData("energya", avgEnergya);
                graphPanel.addData("speeda", avgSpeeda);
                graphPanel.addData("aggroa", avgAggressiona);
                graphPanel.addData("fitnessp", avgFitnessp);
                graphPanel.addData("energyp", avgEnergyp);
                graphPanel.addData("speedp", avgSpeedp);
                graphPanel.addData("aggrop", avgAggressionp);
                graphPanel.addData("food", food.size());
                graphPanel.addData("popa", popa.organisms.size());
                graphPanel.addData("pred", predators.predators.size());

                statsPanel.sendData(avgFitnessa, avgEnergya, avgSpeeda, avgAggressiona, avgFitnessp, avgEnergyp, avgSpeedp, avgAggressionp, food.size(), popa.organisms.size(), predators.predators.size());

                popPanel.colonya = popa.organisms;
            }

            popPanel.repaint();
        });
        timer.start();
    }
}