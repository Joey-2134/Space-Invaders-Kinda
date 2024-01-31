import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    public final int FRAME_TIME = 20;
    public final int ALIENS_PER_LINE = 5;
    public final int NUM_ALIENS = ALIENS_PER_LINE * ALIENS_PER_LINE;
    public final int ALIEN_WIDTH = 50;
    public static final int GRID_SPACER_X = 20;
    public static final double GRID_SPACER_Y = 50;
    public final int PLAYER_WIDTH = 54;
    public static final int BORDER_OFFSET = 100;
    public final int PLAYER_VELOCITY = 10;
    public final int ALIEN_VELOCITY = 5;
    public final char LEFT_KEY = 'a';
    public final char RIGHT_KEY = 'd';
    public static final int WINDOW_SIZE_X = 1200;
    public static final int WINDOW_SIZE_Y = 900;

    private static final Dimension WindowSize = new Dimension(WINDOW_SIZE_X, WINDOW_SIZE_Y); // Updated window size

    private final Alien[] aliens = new Alien[NUM_ALIENS];
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

        for (int i = 0, yPosIterator = 0; i < NUM_ALIENS; i++) {
            Alien alien = new Alien(new ImageIcon("Assets/alien_ship_1.png"), WindowSize.width);
            aliens[i] = alien;
            if (i % ALIENS_PER_LINE == 0) yPosIterator++;

            double yPos = GRID_SPACER_Y * yPosIterator;

            double totalGridWidth = (ALIEN_WIDTH + GRID_SPACER_X) * (ALIENS_PER_LINE - 1);
            double gridStartX = (WindowSize.width - totalGridWidth) / 2;
            double xPos = gridStartX + ((ALIEN_WIDTH + GRID_SPACER_X) * (i % ALIENS_PER_LINE)) - (ALIEN_WIDTH /2);

            aliens[i].setPosition(xPos, yPos);
        }
        player.setPosition((double) WindowSize.width / 2 - (PLAYER_WIDTH / 2), WindowSize.height - BORDER_OFFSET); // Center player in new resolution

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
        if (Character.toLowerCase(e.getKeyChar()) == LEFT_KEY) {
            player.setXVel(-PLAYER_VELOCITY);
        } else if (Character.toLowerCase(e.getKeyChar()) == RIGHT_KEY) {
            player.setXVel(PLAYER_VELOCITY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == LEFT_KEY || e.getKeyChar() == RIGHT_KEY) {
            player.setXVel(0); //stops player moving when a or d is released
        }
    }

    @Override
    public void run() {
        while (true) { //game loop

            Alien.invertDirection(aliens);
            for (Alien alien : aliens) { //every thread loop, player and alien is moved
                alien.setXVel(ALIEN_VELOCITY);
                alien.moveEnemy();
            }
            player.movePlayer();
            repaint(); //frame is repainted
            try {
                Thread.sleep(FRAME_TIME); // 50 fps
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
         }

    }
    public static void main(String[] args) {
        new InvadersApplication();
    }
}
