import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Window implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 640;
    public static final int TANK_WIDTH = 25;
    private double t = 0;
    private HashSet<Integer> pressedKeys;
    private Tank activeTank;
    private Tank.Bullet bullet;
    private Wind wind;
    private boolean gameEnd;

    private class Panel extends JPanel {
        private ArrayList<Integer> terrainHeights;
        private int subdivision;

        public Panel() {
            Terrain terrain = new Terrain(Window.HEIGHT/2 - 100, 8);
            terrain.generateTerrain();
            terrainHeights = terrain.getHeights();
            for (int i = 0; i < terrainHeights.size(); i ++) {
                terrainHeights.set(i, terrainHeights.get(i) + Window.HEIGHT/2);
            }
            subdivision = (int) Math.round((Window.WIDTH + 0.0)/(terrainHeights.size()-1));

            for (int i = 0; i < numPlayers; i ++) {
                new Tank();
            }
            for (Tank t : Tank.getAllTanks()) {
                initTankHeight(t);
                initTankAngle(t);
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            if (gameEnd) {
                g.drawString("Game ended. Player " + Tank.getAllTanks().get(0).getId() + " has won.",
                        Window.WIDTH/2 - 80, Window.HEIGHT/2);
            }
            else if (!gameEnd) {
                drawTerrain(g);
                for (Tank tank : Tank.getAllTanks()) {
                    drawTank(g, tank);
                }
                drawBullet(g);
                drawPowerBar(g);
                drawWindBar(g);
            }
        }

        // Connects the dots in terrainHeights
        public void drawTerrain(Graphics g) {
            for (int i = 0; i < terrainHeights.size() - 1; i ++) {
                g.drawLine(i * subdivision, terrainHeights.get(i),
                        (i + 1) * subdivision, terrainHeights.get(i + 1));
            }
        }

        private void initTankHeight(Tank tank) {
            int x = tank.getX();
            int lowerBound = x/subdivision;
            int y = Terrain.cosineInterpolate(terrainHeights.get(lowerBound),
                    terrainHeights.get(lowerBound + 1), (x - subdivision * lowerBound)/(0.0 + subdivision))
                    - TANK_WIDTH*2/3;
            tank.setY(y);
        }

        private void initTankAngle(Tank tank) {
            // to draw the tank normal to the terrain, we rotate Graphics g by the
            // inverse tangent of the slope of the hill under the middle of the tank
            int x = tank.getX();
            int lowerBound = x/subdivision;
            double slope = -(0.0 + terrainHeights.get(lowerBound + 1) - terrainHeights.get(lowerBound))/
                    subdivision;
            tank.setTankAngle(Math.atan(slope));
        }

        public void drawTank(Graphics g, Tank tank) {
            Graphics2D g2 = (Graphics2D) g;

            int x = tank.getX();
            int y = tank.getY();

            double angle = tank.getMuzzleAngle();

            double tankRotationAngle = tank.getTankAngle();
            g2.rotate(-tankRotationAngle, x, y + TANK_WIDTH/3);

            g2.drawRect(x-TANK_WIDTH/2, y + TANK_WIDTH*2/3 + 10, TANK_WIDTH, 5);
            g2.setColor(Color.RED);
            g2.fillRect(x - TANK_WIDTH/2, y + TANK_WIDTH*2/3 + 10,
                    (int) (TANK_WIDTH * tank.getHealth()/100.0), 5);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x - TANK_WIDTH/2,y,
                    TANK_WIDTH,TANK_WIDTH*2/3,TANK_WIDTH/3,TANK_WIDTH/3);
            g2.drawString(Integer.toString(tank.getId()), x, y + 15);
            g2.rotate(-angle, x + 1, y - 3);
            g2.drawRect(x + 1, y-6, 16, 6);
            g2.rotate(angle, x + 1, y - 3);

            g2.rotate(tankRotationAngle, x, y + TANK_WIDTH/3);
        }

        public void drawBullet(Graphics g) {
            int event = bullet.bulletCollision(terrainHeights);
            if (event >= 2) {
                Tank tank = Tank.getAllTanks().get(event - 2);
                tank.setHealth(tank.getHealth() - 25);
                checkDeath(tank);
                nextTurn();
                return;
            }
            if (event == 1) {
                nextTurn();
                return;
            }
            g.fillOval(bullet.xTrajectory(t, wind), bullet.yTrajectory(t), 4, 4);
        }

        public void drawPowerBar(Graphics g) {
            g.drawRect(Window.WIDTH/2 - 100, Window.HEIGHT - 100, 200, 50);
            g.setColor(Color.RED);
            g.fillRect(Window.WIDTH/2 - 100, Window.HEIGHT - 100, (int) (activeTank.getPower() * 200), 50);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString((int) (activeTank.getPower() * 100)), Window.WIDTH/2 - 90, Window.HEIGHT - 75);
        }

        public void drawWindBar(Graphics g) {
            g.drawRect(Window.WIDTH/2 - 100, 25, 200, 35);
            g.setColor(Color.CYAN);
            if (wind.getVelocity() < 0) {
                g.fillRect(Window.WIDTH/2 + (int) (wind.getVelocity()*100.0/15), 26,
                        (int) -(wind.getVelocity()*100.0/15), 34);
            }
            else if (wind.getVelocity() > 0) {
                g.fillRect(Window.WIDTH/2, 26, (int) (wind.getVelocity()*100.0/15), 34);
            }
            g.setColor(Color.BLACK);
            g.drawString(Double.toString(Helper.round(wind.getVelocity(), 2)), Window.WIDTH/2 - 90, 42);
        }

        public void win() {
            gameEnd = true;
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
        wind = new Wind();
        bullet = activeTank.new Bullet();

        gameEnd = false;
        running = false;
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
            running = true;
            start();
            return;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) activeTank.incMuzzleAngle(0.05);
        if (pressedKeys.contains(KeyEvent.VK_D)) activeTank.incMuzzleAngle(-0.05);
        if (pressedKeys.contains(KeyEvent.VK_W)) activeTank.incPower(0.01);
        if (pressedKeys.contains(KeyEvent.VK_S)) activeTank.incPower(-0.01);

        panel.repaint();
    }

    public void checkDeath(Tank tank) {
        if (tank.getHealth() <= 0) {
            Tank.getAllTanks().remove(tank);
        }
        if (Tank.getAllTanks().size() == 1) {
            panel.win();
        }
    }

    public void nextTurn() {
        activeTank.bulletLanded();
        Game.nextTurn();
        activeTank = Tank.getAllTanks().get(Game.getTurn());
        bullet = activeTank.new Bullet();
        wind.generateNewWind();

        running = false;
        t = 0;

        panel.repaint();
    }

    private void start() {
        th = new Thread(this);
        th.start();
    }

    private volatile boolean running;

    @Override
    public void run() {
        while (running) {
            t += 0.05;
            panel.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
