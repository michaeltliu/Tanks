import java.util.ArrayList;
import java.util.Random;

public class Tank {
    private final int STARTING_HEALTH = 100;
    private final int MAXIMUM_POWER = 50;       // think of units as m/s
    private int health;
    private int x;
    private double muzzleAngle;     // radian angle at which the tank's gun muzzle points
    private double power;       // as a percent of the tank's maximum power
    private boolean lockedMuzzle;     // prevents muzzle angle/power from being adjusted while bullet is in flight
    private static ArrayList<Integer> tankPositions = new ArrayList<>();
    private static ArrayList<Tank> allTanks = new ArrayList<>();    //maintains ArrayList of all Tank objects created

    public Tank() {
        health = STARTING_HEALTH;

        int openLength = Window.WIDTH - Window.TANK_WIDTH;
        Random rand = new Random();
        x = rand.nextInt(openLength);
        muzzleAngle = Math.PI/3;
        power = 0.0;
        lockedMuzzle = false;

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
    // returns the x coordinate of the tank's bullet as a function of time
    public int xtrajectory(int t) {
        return (int) Math.round(power * MAXIMUM_POWER * t * Math.cos(muzzleAngle)) + x;
    }

    // returns the y coordinate of the tank's bullet relative to this Tank's y coordinate
    public int yRelativeTrajectory(int t) {
        return (int) Math.round(power * MAXIMUM_POWER * t * Math.sin(muzzleAngle) -
                4.9 * Math.pow(t, 2));
    }

    public void fireBullet() {
        lockedMuzzle = true;
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
        if (!lockedMuzzle && muzzleAngle + angle <= Math.PI && muzzleAngle + angle >= 0)
            muzzleAngle += angle;
    }
    public double getPower() {
        return power;
    }
    public void incPower(double power) {
        if (!lockedMuzzle && this.power + power > 0 && this.power + power <= 1)
            this.power += power;
    }

}
