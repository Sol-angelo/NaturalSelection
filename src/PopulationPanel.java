import javax.swing.*;
import java.awt.*;
import java.util.List;

class PopulationPanel extends JPanel {
    List<Organism> colonya;
    List<Organism> colonyb;
    List<Predator> predators;
    List<Food> food;

    public PopulationPanel(List<Organism> colonya, List<Organism> colonyb, List<Predator> predators, List<Food> food) {
        this.colonya = colonya;
        this.colonyb = colonyb;
        this.predators = predators;
        this.food = food;
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(176, 227, 147));
    }

    public PopulationPanel(List<Organism> colonya, List<Predator> predators, List<Food> food) {
        this.colonya = colonya;
        this.predators = predators;
        this.food = food;
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(176, 227, 147));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Organism o : colonya) {
            int size = 3;
            g.setColor(Color.BLUE);
            g.fillOval((int) o.x, (int) o.y, size, size);
        }
        for (Organism o : colonyb) {
            int size = 3;
            g.setColor(new Color(0, 178, 255));
            g.fillOval((int) o.x, (int) o.y, size, size);
        }

        for (Predator p : predators) {
            int size = 5;
            g.setColor(Color.RED);
            g.fillOval((int) p.x, (int) p.y, size, size);
        }

        // draw food as yellow dots
        g.setColor(Color.YELLOW);
        for (Food f : food) {
            g.fillRect((int) f.x, (int) f.y, 4, 4);
        }
    }
}