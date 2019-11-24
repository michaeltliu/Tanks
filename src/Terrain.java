import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Terrain {
    private int amplitude;
    private int granularity;    // granularity: how many random points are added in the first pass
    private LinkedList<Integer> heights;        // 50 seems to be plenty
    private Random rand;

    public Terrain(int amplitude, int granularity) {
        this.amplitude = amplitude;
        this.granularity = granularity;
        heights = new LinkedList<>();
        rand = new Random();
    }

    public LinkedList<Integer> getHeights() {
        return heights;
    }

    public void generateTerrain() {
        firstPass();
        smoothing();
        cosineInterpolate();
    }

    private void firstPass() {
        for (int i = 0; i < granularity; i ++) {
            heights.add(rand.nextInt(2*amplitude) - amplitude);
        }
    }

    private void smoothing() {
        ListIterator<Integer> iter = heights.listIterator();
        int prev = iter.next();
        while (iter.hasNext()) {
            //TODO
        }
    }

    private void cosineInterpolate() {

    }

}
