import javax.swing.*;

public class Alien extends Sprite2D {
    private static final int WINDOW_WIDTH = InvadersApplication.WINDOW_SIZE_X;
    //private static final int WINDOW_HEIGHT = InvadersApplication.WINDOWSIZEY;
    private static int directionModifier = 1;

    public Alien(ImageIcon img, int windowWidth) {
        super(img, windowWidth);
    }

    public void moveEnemy() {
        x += xVel * directionModifier;
    }

    public static void invertDirection(Alien[] aliens) {
        for (Alien alien : aliens) {
            if ((alien.x >= WINDOW_WIDTH - InvadersApplication.BORDER_OFFSET && directionModifier == 1) || (alien.x <= InvadersApplication.BORDER_OFFSET && directionModifier == -1)) {
                directionModifier *= -1;
                moveDown(aliens);
            }
        }
    }
    public static void moveDown(Alien[] aliens) {
        for (Alien alien : aliens) {
            alien.y += InvadersApplication.GRID_SPACER_Y;
        }
    }
}
