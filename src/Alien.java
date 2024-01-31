import javax.swing.*;

public class Alien extends Sprite2D {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 900;
    private static int directionModifier = 1;

    public Alien(ImageIcon img, int windowWidth) {
        super(img, windowWidth);
    }

    public void moveEnemy() {
        x += xVel * directionModifier;
    }

    public static void invertDirection(Alien[] aliens) {
        for (Alien alien : aliens) {
            if ((alien.x >= WINDOW_WIDTH - 100 && directionModifier == 1) || (alien.x <= 100 && directionModifier == -1)) {
                directionModifier *= -1;
                moveDown(aliens);
            }
        }
    }
    public static void moveDown(Alien[] aliens) {
        for (Alien alien : aliens) {
            alien.y += 30;
        }
    }
}
