import javax.swing.*;
import java.awt.*;

public class Window implements Runnable {

    int t = 0;

    private class Panel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            g.drawString("Hello", 100+10*t, 100);
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

        frame.setSize(840, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        System.out.println("this was called");
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
