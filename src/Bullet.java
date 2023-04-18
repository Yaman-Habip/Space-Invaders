import java.awt.Rectangle;

public class Bullet {
    public int xpos;
    public int ypos;
    public Rectangle rect;

    public Bullet(int x, int y){
        xpos = x;
        ypos = y;
    }

    public void build_rect(){
        rect = new Rectangle(xpos, ypos, 10,10);
    }

    public void move(){
        ypos -= 10;
        build_rect();
    }
}