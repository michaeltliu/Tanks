import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Window implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 640;
    public static final int TANK_WIDTH = 25;
    private int t = 0;
    private HashSet<Integer> pressedKeys;
    private Tank activeTank;

    private class Panel extends JPanel {
        ArrayList<Integer> terrainHeights;
        int subdivision;

        public Panel() {
            Terrain terrain = new Terrain(Window.HEIGHT/2 - 100, 8);
            terrain.generateTerrain();
            terrainHeights = terrain.getHeights();
            for (int i = 0; i < terrainHeights.size(); i ++) {
                terrainHeights.set(i, terrainHeights.get(i) + Window.HEIGHT/2);
            }
            //subdivision = Math.round((Window.WIDTH + 0f)/(terrainHeights.size()-1));
            subdivision = Window.WIDTH/(terrainHeights.size()-1);

            for (int i = 0; i < numPlayers; i ++) {
                new Tank();
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            if (t < 250) g.drawString("Welcome to Tanks!!!", 50+2*t, 100);
            drawTerrain(g);
            for (Tank t : Tank.getAllTanks()) {
                drawTank(g, t);
            }
        }

        // Connects the dots in terrainHeights
        public void drawTerrain(Graphics g) {
            for (int i = 0; i < terrainHeights.size() - 1; i ++) {
                g.drawLine(i * subdivision, terrainHeights.get(i),
                        (i + 1) * subdivision, terrainHeights.get(i + 1));
            }
        }

        public void drawTank(Graphics g, Tank tank) {
            Graphics2D g2 = (Graphics2D) g;

            int x = tank.getX();
            int lowerBound = x/subdivision;
            int y = Terrain.cosineInterpolate(terrainHeights.get(lowerBound),
                    terrainHeights.get(lowerBound + 1), (x - subdivision * lowerBound)/(0.0 + subdivision))
                    - TANK_WIDTH*2/3;

            double angle = tank.getMuzzleAngle();

            // to draw the tank normal to the terrain, we rotate Graphics g by the
            // inverse tangent of the slope of the hill under the middle of the tank
            double slope = -(0.0 + terrainHeights.get(lowerBound + 1) - terrainHeights.get(lowerBound))/
                    subdivision;
            double tankRotationAngle = Math.atan(slope);
            g2.rotate(-tankRotationAngle, x, y + TANK_WIDTH/3);

            g2.drawRoundRect(x - TANK_WIDTH/2,y,
                    TANK_WIDTH,TANK_WIDTH*2/3,TANK_WIDTH/3,TANK_WIDTH/3);
            g2.rotate(-angle, x + 1, y - 3);
            g2.drawRect(x + 1, y-6, 16, 6);
            g2.rotate(angle, x + 1, y - 3);

            g2.rotate(tankRotationAngle, x, y + TANK_WIDTH/3);
        }

        public void drawBullet(Graphics g) {

        }

        public ArrayList<Integer> getTerrainHeights() {
            return terrainHeights;
        }
    }

    private JFrame frame;
    private Panel panel;
    private Thread th;
    private int numPlayers;

    public Window(int count) {
        numPlayers = count;
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
        frame.addKeyListener(this);

        pressedKeys = new HashSet<>();
        activeTank = Tank.getAllTanks().get(Game.getTurn());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        keyActions();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        keyActions();
    }

    private void keyActions() {
        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            activeTank.fireBullet();
            start();
            return;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) activeTank.incMuzzleAngle(0.05);
        if (pressedKeys.contains(KeyEvent.VK_D)) activeTank.incMuzzleAngle(-0.05);
        if (pressedKeys.contains(KeyEvent.VK_W)) activeTank.incPower(0.01);
        if (pressedKeys.contains(KeyEvent.VK_S)) activeTank.incPower(-0.01);

        panel.repaint();
    }
    private void start() {
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        while (true) {
            t++;
            panel.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
