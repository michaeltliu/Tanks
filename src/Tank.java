import java.util.ArrayList;
import java.util.Random;

public class Tank {
    public class Bullet {
        private int bulletX;    // saves the most recent coordinates of the bullet
        private int bulletY;    // reduces duplicate computation

        public Bullet() {
            bulletX = x;
            bulletY = y;
        }

        // r = <v * t * cos(theta), v * t * sin(theta) - 4.9 * t^2> + starting position
        // returns the x coordinate of the tank's bullet as a function of time
        public int xTrajectory(double t, Wind wind) {
            int bx = (int) Math.round(power * MAXIMUM_POWER * t * Math.cos(muzzleAngle + tankAngle) +
                    wind.getVelocity() * t) + x;
            bulletX = bx;
            return bulletX;
        }

        // returns the y coordinate of the tank's bullet relative to this Tank's y coordinate
        public int yTrajectory(double t) {
            int by = (int) -Math.round(power * MAXIMUM_POWER * t * Math.sin(muzzleAngle + tankAngle) -
                    4.9 * Math.pow(t, 2)) + y;
            bulletY = by;
            return bulletY;
        }

        // return 0 if no collision
        // return 1 if ground collision
        // return 2 + i if collision with the i-th Tank in Tank.allTanks()
        public int bulletCollision(ArrayList<Integer> terrainHeights) {
            int subdivision = (int) Math.round((Window.WIDTH + 0.0)/(terrainHeights.size()-1));
            int lowerBound = bulletX/subdivision;

            for (int i = 0; i < Tank.allTanks.size(); i ++) {
                Tank tank = allTanks.get(i);
                if (tank == Tank.this) continue;
                else if (bulletX > tank.getX() - Window.TANK_WIDTH/2 &&
                        bulletX < tank.getX() + Window.TANK_WIDTH/2 &&
                        bulletY > tank.getY() && bulletY < tank.getY() + Window.TANK_WIDTH*2/3) {
                    return 2 + i;
                }
            }

            if (lowerBound < 0 || lowerBound >= terrainHeights.size() ||
                    bulletY > terrainHeights.get(lowerBound)) return 1;

            return 0;
        }
    }

    private final int STARTING_HEALTH = 100;
    private final int MAXIMUM_POWER = 100;       // think of units as m/s
    private int health;
    private int x;
    private int y;
    private double tankAngle;       // radian angle at which the tank is tilted on the terrain
    private double muzzleAngle;     // radian angle of tank's gun muzzle relative to tank's body
    private double power;       // as a percent of the tank's maximum power
    private boolean lockedMuzzle;     // prevents muzzle angle/power from being adjusted while bullet is in flight
    private static ArrayList<Integer> tankPositions = new ArrayList<>();
    private static ArrayList<Tank> allTanks = new ArrayList<>();    //maintains ArrayList of all Tank objects created

    public Tank() {
        health = STARTING_HEALTH;

        int openLength = Window.WIDTH - Window.TANK_WIDTH;      // ensures tank doesn't extend beyond right edge
        Random rand = new Random();
        x = rand.nextInt(openLength);
        muzzleAngle = Math.PI/3;
        power = 0.5;
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

    public void fireBullet() {
        lockedMuzzle = true;
    }

    public void bulletLanded() {
        lockedMuzzle = false;
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
    public int getY() {
        return y;
    }
    public void setY(int newY) {
        y = newY;
    }
    public double getTankAngle() {
        return tankAngle;
    }
    public void setTankAngle(double angle) {
        tankAngle = angle;
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
        if (!lockedMuzzle && this.power + power > 0 && this.power + power < 1.0000001)      // double rounding error
            this.power += power;
    }

}
