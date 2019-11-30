import java.util.ArrayList;
import java.util.Random;

public class Terrain {
    private int amplitude;
    private int granularity;    // granularity: how many random points are added in the first pass
    private ArrayList<Integer> heights;
    private Random rand;

    public Terrain(int amplitude, int granularity) {
        this.amplitude = amplitude;
        this.granularity = granularity;
        heights = new ArrayList<>();
        rand = new Random();
    }

    public ArrayList<Integer> getHeights() {
        return heights;
    }

    public void generateTerrain() {
        firstPass();
        smoothing();
        cosineInterpolate();
    }

    public void firstPass() {
        for (int i = 0; i < granularity; i ++) {
            heights.add(rand.nextInt(2*amplitude) - amplitude);
        }
    }

    public void smoothing() {
        ArrayList<Integer> copy = new ArrayList<>(heights);
        heights.set(0, Math.round(3f/4 * copy.get(0) + 1f/4 * copy.get(1)));
        heights.set(granularity - 1, Math.round(3f/4 * copy.get(granularity - 1) +
                1f/4 * copy.get(granularity - 2)));
        for (int i = 1; i < granularity - 1; i ++) {
            heights.set(i, Math.round(copy.get(i - 1)/4f + copy.get(i)/2f + copy.get(i + 1)/4f));
        }
    }

    // Returns a new height provided an new x coordinate nx at which to interpolate
    // nx is 0 if it coincides with the x1 and 1 if it coincides with x2
    public static int cosineInterpolate(int y1, int y2, double nx) {
        double nx2 = (1 - Math.cos(nx * Math.PI))/2;
        return (int) Math.round(y1 * (1 - nx2) + y2 * nx2);
    }

    // Calls cosineInterpolate(int, int, int) for many new x coordinates
    public void cosineInterpolate() {
        ArrayList<Integer> copy = new ArrayList<>(heights);
        for (int i = 0; i < granularity - 1; i ++) {
            for (int j = 1; j < 16; j ++) {
                heights.add(16 * i + j, cosineInterpolate(copy.get(i), copy.get(i + 1),
                        j / 16.0));
            }
        }
    }

}