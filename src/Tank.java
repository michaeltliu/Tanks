import java.util.ArrayList;
import java.util.Random;

public class Tank {
    private final int STARTING_HEALTH = 100;
    private int health;
    private int x;
    private static ArrayList<Integer> tankPositions;
    private static ArrayList<Tank> allTanks;

    public Tank() {
        health = STARTING_HEALTH;

        int openLength = Window.WIDTH - (Window.TANK_WIDTH + 12) * tankPositions.size();
        Random rand = new Random();
        int preX = rand.nextInt(openLength);
        x = preX;
        for (Integer i : tankPositions) {
            // Guarantees tanks are not randomly dropped too close to each other
            if (preX > i.intValue() - 6) x += Window.TANK_WIDTH + 12;
        }
        tankPositions.add(x);

        allTanks.add(this);
    }


    public static ArrayList<Integer> getTankPositions() {
        return tankPositions;
    }
    public static ArrayList<Tank> getAllTanks() {
        return allTanks;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int h) {
        health = h;
    }
    public int getX() {
        return x;
    }
    public void setX(int newX) {
        x = newX;
    }


}
