import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StatsPanel extends JPanel {
    double fitnessa = 0;
    double fitnessb = 0;
    double energya = 0;
    double energyb = 0;
    double speeda = 0;
    double speedb = 0;
    double aggroa = 0;
    double aggrob = 0;
    int food = 0;
    int popa = 0;
    int popb = 0;
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
        g.drawString("Population A", 20, 40);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Avg fitness: " + String.format("%.2f", fitnessa), 20, 60);
        g.drawString("Avg energy: " + String.format("%.2f", energya), 20, 75);
        g.drawString("Avg speed: " + String.format("%.2f", speeda), 20, 90);
        g.drawString("Avg aggression: " + String.format("%.2f", aggroa), 20, 105);
        g.drawString("Population: " + popa, 20, 120);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Population B", 20, 150);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Avg fitness: " + String.format("%.2f", fitnessb), 20, 170);
        g.drawString("Avg energy: " + String.format("%.2f", energyb), 20, 185);
        g.drawString("Avg speed: " + String.format("%.2f", speedb), 20, 200);
        g.drawString("Avg aggression: " + String.format("%.2f", aggrob), 20, 215);
        g.drawString("Population: " + popb, 20, 230);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Environment", 20, 260);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Food: " + food, 20, 280);
        g.drawString("Predators: " + pred, 20, 295);
    }

    public void sendData(double avgFitnessa, double avgEnergya, double avgSpeeda, double avgAggressiona, double avgFitnessb, double avgEnergyb, double avgSpeedb, double avgAggressionb,int size, int size1, int size2, int size3) {
        fitnessa = avgFitnessa;
        energya = avgEnergya;
        speeda = avgSpeeda;
        aggroa = avgAggressiona;
        fitnessb = avgFitnessb;
        energyb = avgEnergyb;
        speedb = avgSpeedb;
        aggrob = avgAggressionb;
        food = size;
        popa = size1;
        popb = size2;
        pred = size3;
        repaint();
    }
}