import javax.swing.*;

public class Spaceship extends Sprite2D{
    public Spaceship(ImageIcon img, int windowWidth) {
        super(img, windowWidth);
    }
    public void movePlayer() {  //moves player along the x axis, a positive or negative number based on what key is pressed
        if (x <= windowWidth - InvadersApplication.BORDER_OFFSET && x >= InvadersApplication.BORDER_OFFSET) {
            x += xVel;
        }
    }
}
