import javax.swing.*;

public class Bullet extends Sprite2D{

    public Bullet(ImageIcon img, ImageIcon img2, int windowWidth) {
        super(img, img2, windowWidth);
    }

    public void move() {
        y -= 10;
    }

}
