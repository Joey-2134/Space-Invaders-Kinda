import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    private static final Dimension WindowSize = new Dimension(1000,1000);
    private static final int NUMALIENS = 15; // Number of GameObjects
    private Sprite2D[] aliens = new Sprite2D[NUMALIENS];
    private Sprite2D player = new Sprite2D(new ImageIcon("Assets/player_ship.png"));

    public InvadersApplication() {
        this.setTitle("Assignment 3"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation

        // Center the window on the screen
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width/2 - WindowSize.width/2;
        int y = screenSize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height); // Set position and size of the window
        setVisible(true); // Make the window visible

        Thread t = new Thread(this);
        t.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1000,1000);
        g.drawImage(player);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {

        }
    }
    public static void main(String[] args) {
        new InvadersApplication();
    }
}
