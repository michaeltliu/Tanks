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

            for (int i = 0; i < 10; i ++) {
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
        public void drawTerrain(Graphics g) {
            for (int i = 0; i < terrainHeights.size() - 1; i ++) {
                g.drawLine(i * subdivision, terrainHeights.get(i),
                        (i + 1) * subdivision, terrainHeights.get(i + 1));
            }
        }

        // angle parameter is the radian angle at which the tank's gun muzzle points
        // TODO: reduce error between tank y position and the terrain height
        // TODO: draw the tank normal to the terrain
        public void drawTank(Graphics g, Tank tank) {
            int x = tank.getX();
            double angle = tank.getMuzzleAngle();
            int y = terrainHeights.get(x/subdivision) - TANK_WIDTH*2/3;
            g.drawRoundRect(x,y,TANK_WIDTH,TANK_WIDTH*2/3,TANK_WIDTH/3,TANK_WIDTH/3);
            //drawTankNozzle(g, x + TANK_WIDTH/2, y, angle);
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

        frame.addKeyListener(this);
        pressedKeys = new HashSet<>();
        start();
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
        Tank activeTank = Tank.getAllTanks().get(Game.getTurn());
        for (Integer i : pressedKeys) {
            if (i == KeyEvent.VK_KP_LEFT) activeTank.incMuzzleAngle(0.05);
            else if (i == KeyEvent.VK_KP_RIGHT) activeTank.incMuzzleAngle(-0.05);
            else if (i == KeyEvent.VK_KP_UP) activeTank.incPower(0.01);
            else if (i == KeyEvent.VK_KP_DOWN) activeTank.incPower(-0.01);
        }
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
