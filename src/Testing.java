public class Testing {
    public static void main(String[] args) {
        Terrain terrain = new Terrain(50, 10);
        terrain.firstPass();
        System.out.println(terrain.getHeights());
        terrain.smoothing();
        System.out.println(terrain.getHeights());
        terrain.cosineInterpolate();
        System.out.println(terrain.getHeights());
    }
}
