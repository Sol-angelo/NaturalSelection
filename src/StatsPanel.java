import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StatsPanel extends JPanel {
    double fitnessa = 0;
    double energya = 0;
    double speeda = 0;
    double aggroa = 0;
    double visiona = 0;
    double fitnessp = 0;
    double energyp = 0;
    double speedp = 0;
    double aggrop = 0;
    int food = 0;
    int popa = 0;
    int pred = 0;

    public StatsPanel() {
        setBackground(Color.BLACK);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Label latest value
        g.setColor(Color.WHITE); // white text on black background
        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Prey", 20, 40);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Avg fitness: " + String.format("%.2f", fitnessa), 20, 60);
        g.drawString("Avg energy: " + String.format("%.2f", energya), 20, 75);
        g.drawString("Avg speed: " + String.format("%.2f", speeda), 20, 90);
        g.drawString("Avg aggression: " + String.format("%.2f", aggroa), 20, 105);
        g.drawString("Avg vision: " + String.format("%.2f", visiona), 20, 120);
        g.drawString("Population: " + popa, 20, 135);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Predators", 20, 165);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Avg fitness: " + String.format("%.2f", fitnessp), 20, 185);
        g.drawString("Avg energy: " + String.format("%.2f", energyp), 20, 200);
        g.drawString("Avg speed: " + String.format("%.2f", speedp), 20, 215);
        g.drawString("Avg aggression: " + String.format("%.2f", aggrop), 20, 230);
        g.drawString("Population: " + pred, 20, 245);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Environment", 20, 275);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Food: " + food, 20, 295);
    }

    public void sendData(double avgFitnessa, double avgEnergya, double avgSpeeda, double avgAggressiona, double avgVisiona, double avgFitnessp, double avgEnergyp, double avgSpeedp, double avgAggressionp, int size, int size1, int size3) {
        fitnessa = avgFitnessa;
        energya = avgEnergya;
        speeda = avgSpeeda;
        aggroa = avgAggressiona;
        visiona = avgVisiona;
        fitnessp = avgFitnessp;
        energyp = avgEnergyp;
        speedp = avgSpeedp;
        aggrop = avgAggressionp;
        food = size;
        popa = size1;
        pred = size3;
        repaint();
    }
}