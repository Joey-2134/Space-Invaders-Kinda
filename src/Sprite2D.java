import javax.swing.*;
import java.awt.*;

public abstract class Sprite2D {
    protected double x,y;
    protected double xVel = 0;
    protected Image myImage;
    protected Image myImage2;
    protected int windowWidth;
    protected static int framesDrawn = 0; // Static variable shared by all instances

    public Sprite2D(ImageIcon img, ImageIcon img2, int windowWidth) {
        myImage = img.getImage(); //sprite given respective image file
        myImage2 = img2.getImage();
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
        framesDrawn++;
        if ( framesDrawn%100<50 )
            g.drawImage(myImage, (int)x, (int)y, null);
        else
            g.drawImage(myImage2, (int)x, (int)y, null);
    }
}
