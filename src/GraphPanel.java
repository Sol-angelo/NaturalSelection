import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class GraphPanel extends JPanel {
    enum DisplayMode { ONE_AT_A_TIME, ALL_STACKED, ALL_OVERLAID }
    private DisplayMode displayMode = DisplayMode.ONE_AT_A_TIME;
    private int currentGraph = 0; // 0=Population, 1=PopA, 2=PopB

    private final Map<String, List<Double>> history = new HashMap<>();
    private final Map<String, Color> colors = new HashMap<>();

    private final int paddingLeft = 40;
    private final int paddingRight = 20;
    private final int paddingTop = 20;
    private final int paddingBottom = 40;

    public GraphPanel() {
        // Population
        history.put("popa", new ArrayList<>());
        history.put("popb", new ArrayList<>());
        history.put("pred", new ArrayList<>());
        history.put("food", new ArrayList<>());
        colors.put("popa", Color.MAGENTA);
        colors.put("popb", Color.CYAN);
        colors.put("pred", Color.ORANGE);
        colors.put("food", Color.YELLOW);

        // Population A stats
        history.put("fitnessa", new ArrayList<>());
        history.put("energya", new ArrayList<>());
        history.put("speeda", new ArrayList<>());
        history.put("aggroa", new ArrayList<>());
        colors.put("fitnessa", Color.RED);
        colors.put("energya", Color.GREEN);
        colors.put("speeda", Color.BLUE);
        colors.put("aggroa", Color.YELLOW);

        // Population B stats
        history.put("fitnessb", new ArrayList<>());
        history.put("energyb", new ArrayList<>());
        history.put("speedb", new ArrayList<>());
        history.put("aggrob", new ArrayList<>());
        colors.put("fitnessb", new Color(255, 100, 100));
        colors.put("energyb", new Color(100, 255, 100));
        colors.put("speedb", new Color(100, 100, 255));
        colors.put("aggrob", new Color(255, 252, 100));

        setBackground(Color.BLACK);

        // Key listener to switch graphs
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (displayMode == DisplayMode.ONE_AT_A_TIME) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        currentGraph = (currentGraph + 1) % 2;
                        repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        currentGraph = (currentGraph + 1) % 2;
                        repaint();
                    }
                }
                // Toggle modes
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    displayMode = DisplayMode.ONE_AT_A_TIME;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    displayMode = DisplayMode.ALL_STACKED;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    displayMode = DisplayMode.ALL_OVERLAID;
                    repaint();
                }
            }
        });
    }

    public void addData(String metric, double value) {
        List<Double> list = history.get(metric);
        if (list == null) return;
        list.add(value);
        if (list.size() > 6400) list.remove(0);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (displayMode == DisplayMode.ALL_STACKED) {
            int h = getHeight() / 3;
            drawGraph(g, 0, 0, getWidth(), h, "Population Sizes",
                    Arrays.asList("popa", "popb", "pred"));
            drawGraph(g, 0, h, getWidth(), h, "Population A Stats",
                    Arrays.asList("fitnessa", "aggroa", "speeda"));
            drawGraph(g, 0, 2 * h, getWidth(), h, "Population B Stats",
                    Arrays.asList("fitnessb", "aggrob", "speedb"));
        } else if (displayMode == DisplayMode.ONE_AT_A_TIME) {
            if (currentGraph == 0) {
                drawGraph(g, 0, 0, getWidth(), getHeight(), "Population Sizes",
                        Arrays.asList("popa", "pred"));
            } else {
                drawGraph(g, 0, 0, getWidth(), getHeight(), "Population A Stats",
                        Arrays.asList("fitnessa", "speeda", "aggroa"));
            }
        } else if (displayMode == DisplayMode.ALL_OVERLAID) {
            drawGraph(g, 0, 0, getWidth(), getHeight(), "All Metrics",
                    new ArrayList<>(history.keySet()));
        }
    }

    private void drawGraph(Graphics g, int x, int y, int w, int h,
                           String title, List<String> metrics) {
        int graphWidth = w - paddingLeft - paddingRight;
        int graphHeight = h - paddingTop - paddingBottom;

        double globalMax = metrics.stream()
                .map(history::get)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .mapToDouble(Double::doubleValue)
                .max().orElse(1);

        int maxPoints = metrics.stream()
                .map(history::get)
                .filter(Objects::nonNull)
                .mapToInt(List::size)
                .max().orElse(1) - 1;

        g.setColor(Color.WHITE);
        g.drawRect(x + paddingLeft, y + paddingTop, graphWidth, graphHeight);
        g.drawString(title, x + w / 2 - g.getFontMetrics().stringWidth(title) / 2, y + 15);

        // Y-axis grid + labels
        int gridLines = 5;
        g.setFont(new Font("Calibri", Font.PLAIN, 10));
        for (int i = 0; i <= gridLines; i++) {
            int yLine = y + paddingTop + i * graphHeight / gridLines;
            g.setColor(new Color(100, 100, 100, 100));
            g.drawLine(x + paddingLeft, yLine, x + paddingLeft + graphWidth, yLine);

            double value = globalMax - (i * globalMax / gridLines);
            g.setColor(Color.WHITE);
            g.drawString(String.format("%.1f", value), x + 2, yLine + 4);
        }

        // Draw data lines
        for (String metric : metrics) {
            List<Double> list = history.get(metric);
            if (list == null || list.size() < 2) continue;

            g.setColor(colors.getOrDefault(metric, Color.GRAY));
            for (int i = 0; i < list.size() - 1; i++) {
                int x1 = x + paddingLeft + i * graphWidth / maxPoints;
                int y1 = y + paddingTop + graphHeight - (int) (list.get(i) / globalMax * graphHeight);
                int x2 = x + paddingLeft + (i + 1) * graphWidth / maxPoints;
                int y2 = y + paddingTop + graphHeight - (int) (list.get(i + 1) / globalMax * graphHeight);
                g.drawLine(x1, y1, x2, y2);
            }
        }

        // Draw legend box
        int legendX = x + w - 110;
        int legendY = y + paddingTop + 10;
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(legendX - 5, legendY - 5, 100, metrics.size() * 15 + 10);
        g.setColor(Color.WHITE);
        g.drawRect(legendX - 5, legendY - 5, 100, metrics.size() * 15 + 10);

        int i = 0;
        for (String metric : metrics) {
            g.setColor(colors.getOrDefault(metric, Color.GRAY));
            g.fillRect(legendX, legendY + i * 15, 10, 10);
            g.setColor(Color.WHITE);
            g.drawString(metric, legendX + 15, legendY + i * 15 + 10);
            i++;
        }
    }
}