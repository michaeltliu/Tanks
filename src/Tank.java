import java.util.ArrayList;
import java.util.Random;

public class Tank {
    private final int STARTING_HEALTH = 100;
    private int health;
    private int x;
    private double muzzleAngle;
    private double power;       // as a percent of the tank's maximum power
    private static ArrayList<Integer> tankPositions = new ArrayList<>();
    private static ArrayList<Tank> allTanks = new ArrayList<>();

    public Tank() {
        health = STARTING_HEALTH;

        int openLength = Window.WIDTH - Window.TANK_WIDTH;
        Random rand = new Random();
        x = rand.nextInt(openLength);
        muzzleAngle = 0.0;
        power = 0.0;

        while (!positionOK(x)) {
            // Guarantees tanks are not randomly dropped too close to each other
            x = rand.nextInt(openLength);
        }
        tankPositions.add(x);

        allTanks.add(this);
    }

    private boolean positionOK(int pos) {
        for (Integer i : tankPositions) {
            if (Math.abs(i - x) < Window.TANK_WIDTH + 6) return false;
        }
        return true;
    }

    // r = <v * t * cos(theta), v * t * sin(theta) - 4.9 * t^2> + starting position
    public int xtrajectory(int t) {

    }

    public int ytrajectory(int t) {

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
    public double getMuzzleAngle() {
        return muzzleAngle;
    }
    public void incMuzzleAngle(double angle) {
        if (muzzleAngle + angle <= Math.PI && muzzleAngle + angle >= 0)
            muzzleAngle += angle;
    }
    public double getPower() {
        return power;
    }
    public void incPower(double power) {
        if (this.power + power > 0 && this.power + power <= 1)
            this.power += power;
    }

}
