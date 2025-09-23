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
        g.drawString("Population: " + popa, 20, 105);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Population B", 20, 135);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Avg fitness: " + String.format("%.2f", fitnessb), 20, 155);
        g.drawString("Avg energy: " + String.format("%.2f", energyb), 20, 170);
        g.drawString("Avg speed: " + String.format("%.2f", speedb), 20, 185);
        g.drawString("Population: " + popb, 20, 200);

        g.setFont(new Font("Calibri", Font.BOLD, 16));
        g.drawString("Environment", 20, 230);

        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        g.drawString("Food: " + food, 20, 250);
        g.drawString("Predators: " + pred, 20, 265);
    }

    public void sendData(double avgFitnessa, double avgEnergya, double avgSpeeda, double avgFitnessb, double avgEnergyb, double avgSpeedb, int size, int size1, int size2, int size3) {
        fitnessa = avgFitnessa;
        energya = avgEnergya;
        speeda = avgSpeeda;
        fitnessb = avgFitnessb;
        energyb = avgEnergyb;
        speedb = avgSpeedb;
        food = size;
        popa = size1;
        popb = size2;
        pred = size3;
        repaint();
    }
}