import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    private static final Dimension WindowSize = new Dimension(1200,900); // Updated window size
    private static final int NUMALIENS = 25;
    private final Alien[] aliens = new Alien[NUMALIENS];
    private final Spaceship player = new Spaceship(new ImageIcon("Assets/player_ship.png"), WindowSize.width);
    private final BufferStrategy strategy;

    public InvadersApplication() {
        this.setTitle("Assignment 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width/2 - WindowSize.width/2;
        int y = screenSize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        for (int i = 0, yPosIterator = 0; i < NUMALIENS; i++) {
            Alien alien = new Alien(new ImageIcon("Assets/alien_ship_1.png"), WindowSize.width);
            aliens[i] = alien;
            if (i % 5 == 0) yPosIterator++;

            double yPos = 50 * yPosIterator;
            double xPos =  440 + (70 * (i % 5)); //todo make starting positions centered in screen automatically alter with screensize change

            aliens[i].setPosition(xPos, yPos);
        }
        player.setPosition((double) WindowSize.width /2 - 25, WindowSize.height - 100); // Center player in new resolution

        addKeyListener(this);

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        Thread t = new Thread(this);
        t.start();
    }

    public void paint(Graphics g) {
        g = strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height); // Fill canvas with new size

        player.paint(g);
        for (Sprite2D alien : aliens) {
            alien.paint(g);
        }
        strategy.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {    //event handlers for when a and d are pressed
        if (e.getKeyChar() == 'a') {
            player.setXVel(-5);
        } else if (e.getKeyChar() == 'd') {
            player.setXVel(5);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'd') {
            player.setXVel(0); //stops player moving when a or d is released
        }
    }

    @Override
    public void run() {
        while (true) { //game loop

            Alien.invertDirection(aliens);
            for (Alien alien : aliens) { //every thread loop, player and alien is moved
                alien.setXVel(5);
                alien.moveEnemy();
            }
            player.movePlayer();

            repaint(); //frame is repainted
            try {
                Thread.sleep(20); // 50 fps
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
         }

    }
    public static void main(String[] args) {
        new InvadersApplication();
    }
}
