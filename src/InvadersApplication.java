import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import static java.lang.System.exit;

public class InvadersApplication extends JFrame implements Runnable, KeyListener{
    //Config settings
    public final int FRAME_TIME = 20;
    public final int ALIENS_PER_LINE = 6;
    public final int NUM_ALIENS = ALIENS_PER_LINE * ALIENS_PER_LINE;
    public final int ALIEN_WIDTH = 50;
    public static final int GRID_SPACER_X = 20;
    public static final double GRID_SPACER_Y = 50;
    public final int PLAYER_WIDTH = 54;
    public static final int BORDER_OFFSET = 100;
    public final int PLAYER_VELOCITY = 15;
    public final int ALIEN_VELOCITY = 15;
    public final char LEFT_KEY = 'a';
    public final char RIGHT_KEY = 'd';
    public final char SHOOT_KEY = 's';
    public static final int WINDOW_SIZE_X = 1200;
    public static final int WINDOW_SIZE_Y = 900;

    private ArrayList<Bullet> bullets = new ArrayList<>(); //array list for storing bullets
    private boolean gameWon = true;
    private boolean isGameOver = false;

    private static final Dimension WindowSize = new Dimension(WINDOW_SIZE_X, WINDOW_SIZE_Y); // Updated window size

    private final Alien[] aliens = new Alien[NUM_ALIENS]; //array for storing alien sprites
    private final Spaceship player = new Spaceship(new ImageIcon("Assets/player_ship.png"),new ImageIcon("Assets/player_ship.png"), WindowSize.width);// player created
    private final BufferStrategy strategy; //double buffering implementation to remove flickering during V-Sync

    public InvadersApplication() {
        this.setTitle("Assignment 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //end program on close
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width/2 - WindowSize.width/2;
        int y = screenSize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);


        resetAlienPositions();  //aliens returned to original position
        resetPlayerPosition(); //sets player position back to centre
        addKeyListener(this); //for key inputs

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        Thread t = new Thread(this);//start the thread
        t.start();
    }

    private void resetAlienPositions() { //resets alien positions for when game is restarted
        for (int i = 0, yPosIterator = 0; i < NUM_ALIENS; i++) {
            //alien array filled with aliens in a grid formation
            Alien alien = new Alien(new ImageIcon("Assets/alien_ship_1.png"),new ImageIcon("Assets/alien_ship_2.png"), WindowSize.width);
            aliens[i] = alien;
            if (i % ALIENS_PER_LINE == 0) yPosIterator++; //increments current row to put aliens on next line

            double yPos = GRID_SPACER_Y * yPosIterator;

            // I hate this, just centers the grid at the beginning
            double totalGridWidth = (ALIEN_WIDTH + GRID_SPACER_X) * (ALIENS_PER_LINE - 1);
            double gridStartX = (WindowSize.width - totalGridWidth) / 2;
            double xPos = gridStartX + ((ALIEN_WIDTH + GRID_SPACER_X) * (i % ALIENS_PER_LINE)) - (ALIEN_WIDTH /2);

            aliens[i].setPosition(xPos, yPos);
        }
    }

    private void resetPlayerPosition() { //resets player position for when game is restarted
        player.setPosition((double) WindowSize.width / 2 - (PLAYER_WIDTH / 2), WindowSize.height - BORDER_OFFSET);
    }

    public void paint(Graphics g) {
        g = strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height); // Fill canvas with new size

        //paint player and all aliens
        if (isGameOver) { //if game over flag is true, run this method
            gameOver(g);
        } else if (gameWon) { //if game won flag is true, run this method
            gameWon(g);
        } else {
            player.paint(g);
            for (Alien alien : aliens) {
                if (alien.isAlive) { //if alien is alive, paint it
                    alien.paint(g);
                }
            }
        }
        for (Bullet bullet : bullets) { //paints all bullets
            bullet.paint(g);
        }
        strategy.show();
    }

    private void gameOver(Graphics g) { //game over screen
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/ArcadeInterlaced-O4d.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            g.setColor(Color.RED);

            Font largeFont = customFont.deriveFont(50f); // Larger font size for "Game Over"
            g.setFont(largeFont);
            g.drawString("Game Over", WindowSize.width / 2 - 225, WindowSize.height / 2);

            Font smallerFont = customFont.deriveFont(30f); // Smaller font size for "Try Again" and "Exit"
            g.setFont(smallerFont);
            g.setColor(Color.GREEN);
            g.drawString("Restart(1)", WindowSize.width / 2 + 50, WindowSize.height / 2 + 100);
            g.drawString("Exit(2)", WindowSize.width / 2 - 250, WindowSize.height / 2 + 100);

        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void gameWon(Graphics g) { //game won screen
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Fonts/ArcadeInterlaced-O4d.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            g.setColor(Color.RED);

            Font largeFont = customFont.deriveFont(50f); // Larger font size for "Game Over"
            g.setFont(largeFont);
            g.drawString("You Win!", WindowSize.width / 2 - 225, WindowSize.height / 2);

        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void restartGame() { //resets game
        isGameOver = false;
        resetPlayerPosition();
        resetAlienPositions();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {    //event handlers for when selected buttons are pressed
        if (!isGameOver) {
            // Existing movement controls
            if (Character.toLowerCase(e.getKeyChar()) == LEFT_KEY) {
                player.setXVel(-PLAYER_VELOCITY);
            } else if (Character.toLowerCase(e.getKeyChar()) == RIGHT_KEY) {
                player.setXVel(PLAYER_VELOCITY);
            } else if (e.getKeyChar() == SHOOT_KEY) {
                shootBullet();
                System.out.println("Bullet shot");
            }
        } else {
            // Additional controls for game over state
            if (e.getKeyChar() == '1') {
                restartGame();
            } else if (e.getKeyChar() == '2') {
                exit(0); // Exit the game
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == LEFT_KEY || e.getKeyChar() == RIGHT_KEY) {
            player.setXVel(0); //stops player moving when movement key is released
        }
    }

    public void shootBullet() { //creates a new bullet and adds it to the array list
        Bullet bullet = new Bullet(new ImageIcon("Assets/bullet.png"), new ImageIcon("Assets/bullet.png"), WindowSize.width);
        bullet.setPosition(player.x + ((double) player.myImage.getWidth(null) / 2), player.y); //sets bullet position to player position
        bullets.add(bullet);
    }

    @Override
    public void run() {
        while (true) { //game loop
            for (Alien alien : aliens) { //checks if all aliens are dead
                if (alien.isAlive) {
                    gameWon = false;
                    break;
                }
                gameWon = true; //if all aliens are dead, game won flag is set to true
            }
            Alien.invertDirection(aliens);  //checks if aliens have hit the border, if so, invert direction and move down
            for (Alien alien : aliens) { //every thread loop, player and alien is moved
                if (alien.y >= player.y - 100) {
                    isGameOver = true; //if aliens reach player, game over flag is set to true
                    break;
                }
                alien.setXVel(ALIEN_VELOCITY); //sets alien speed
                alien.moveEnemy(); //moves alien
            }
            player.movePlayer();

            Iterator<Bullet> bulletIterator = bullets.iterator(); //iterates through bullet array list

            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.move();
                for (int i = 0; i < NUM_ALIENS; i++){
                    if (((bullet.x + bullet.myImage.getWidth(null) > aliens[i].x) && (bullet.x < aliens[i].x + aliens[i].myImage.getWidth(null))) &&
                            ((bullet.y + bullet.myImage.getHeight(null) > aliens[i].y) && (bullet.y < aliens[i].y + aliens[i].myImage.getHeight(null))) && aliens[i].isAlive) { //if bullet hits alien, alien is set to dead and bullet is removed
                        aliens[i].isAlive = false;
                        bulletIterator.remove();
                    }
                }
            }

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
