import java.awt.*;

public class GoodGuy {
    public int xpos = 475;
    public final int ypos = 625;
    public Rectangle rect;
    public void build_rect(){
        int y = (int) Math.round(ypos);
        rect = new Rectangle(xpos, y, 100,100);
    }
    public boolean is_alive = true;
}