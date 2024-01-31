import javax.swing.*;
import java.awt.*;

public abstract class Sprite2D {
    protected double x,y;
    protected double xVel = 0;
    protected Image myImage;
    protected int windowWidth;

    public Sprite2D(ImageIcon img, int windowWidth) {
        myImage = img.getImage(); //sprite given respective image file
        this.windowWidth = windowWidth;
    }

    public void setPosition(double x, double y) { //sets initial sprite pos
        this.x = x;
        this.y = y;
    }

    public void setXVel(double dx) {    //movement speed
        xVel = dx;
    }

    public void paint(Graphics g) { //paints sprite
        g.drawImage(myImage, (int) x, (int) y, null);
    }
}
