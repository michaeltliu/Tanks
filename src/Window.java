import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Window implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 640;
    public static final int TANK_WIDTH = 25;
    int t = 0;

    private class Panel extends JPanel {
        ArrayList<Integer> terrainHeights;
        int subdivision;

        public Panel() {
            Terrain terrain = new Terrain(Window.HEIGHT/2 - 100, 12);
            terrain.generateTerrain();
            terrainHeights = terrain.getHeights();
            for (int i = 0; i < terrainHeights.size(); i ++) {
                terrainHeights.set(i, terrainHeights.get(i) + Window.HEIGHT/2);
            }
            subdivision = Math.round((Window.WIDTH + 0f)/terrainHeights.size());
        }
        public void paint(Graphics g) {
            super.paint(g);
            g.drawString("Hello", 100+10*t, 100);
            drawTerrain(g);
            drawTank(g, 500, 0);
        }
        public void drawTerrain(Graphics g) {
            for (int i = 0; i < terrainHeights.size() - 1; i ++) {
                g.drawLine(i * subdivision, terrainHeights.get(i),
                        (i + 1) * subdivision, terrainHeights.get(i + 1));
            }

            /**
            LinkedList<Integer> terrain = new LinkedList<>();
            Random rand = new Random();
            int current = rand.nextInt(Window.HEIGHT - 200) + 100;
            terrain.add(current);
            for (int i = 1; i <= Window.WIDTH/5; i ++) {
                current =
            }
             */
        }

        // angle parameter is the radian angle at which the tank's gun muzzle points
        // TODO: draw a tank given a Tank object parameter; replace int x and int angle
        // TODO: draw the tank normal to the terrain
        public void drawTank(Graphics g, int x, int angle) {
            int y = terrainHeights.get(x/subdivision) - TANK_WIDTH*2/3;
            g.drawRoundRect(x,y,TANK_WIDTH,TANK_WIDTH*2/3,TANK_WIDTH/3,TANK_WIDTH/3);
            //drawTankNozzle(g, x + TANK_WIDTH/2, y, angle);
        }
    }

    private JFrame frame;
    private Panel panel;
    private Thread th;

    public Window() {
        init();
    }

    private void init() {
        frame = new JFrame("Tanks");
        panel = new Panel();

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        start();
    }

    private void start() {
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(t);
            t++;
            panel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
