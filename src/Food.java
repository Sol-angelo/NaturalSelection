import java.util.Random;

class Food {
    double x, y;
    static Random rand = new Random();

    int age = 0;
    int maxAge = 400;

    public boolean isDead() {
        return age++ > maxAge;
    }

    public Food() {
        x = rand.nextDouble() * 800;
        y = rand.nextDouble() * 600;
    }

    public Food(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Food reproduce(double worldWidth, double worldHeight) {
        double spread = 100; // max distance from parent
        double newX = Math.min(worldWidth, Math.max(0, x + (rand.nextDouble() - 0.5) * spread));
        double newY = Math.min(worldHeight, Math.max(0, y + (rand.nextDouble() - 0.5) * spread));
        return new Food(newX, newY);
    }
}