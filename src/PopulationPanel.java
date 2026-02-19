import javax.swing.*;
import java.awt.*;
import java.util.List;

class PopulationPanel extends JPanel {
    List<Organism> colonya;
    List<Predator> predators;
    List<Food> food;

    public PopulationPanel(List<Organism> colonya, List<Predator> predators, List<Food> food) {
        this.colonya = colonya;
        this.predators = predators;
        this.food = food;
        setPreferredSize(new Dimension(1200, 900));
        setBackground(new Color(176, 227, 147));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Organism o : colonya) {
            int size = 2;
            g.setColor(Color.BLUE);
            g.fillOval((int) o.x, (int) o.y, size, size);
        }

        for (Predator p : predators) {
            int size = 3;
            g.setColor(Color.RED);
            g.fillOval((int) p.x, (int) p.y, size, size);
        }

        // draw food as yellow dots
        g.setColor(Color.YELLOW);
        for (Food f : food) {
            g.fillRect((int) f.x, (int) f.y, 2, 2);
        }
    }
}