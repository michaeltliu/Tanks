import java.util.Random;

public class Wind {
    private Random rand;
    private double velocity;

    public Wind() {
        rand = new Random();
        velocity = (30 * rand.nextDouble()) - 15;
    }

    public void generateNewWind() {
        velocity = (30 * rand.nextDouble()) - 15;
    }

    public double getVelocity() {
        return velocity;
    }
}
