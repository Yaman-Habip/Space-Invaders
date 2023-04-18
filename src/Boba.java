import java.awt.Rectangle;

public class Boba {
    public int xpos;
    public double ypos;
    public Rectangle rect;
    public boolean most_recent = true;

    public Boba(int x, double y){
        xpos = x;
        ypos = y;
    }

    public void build_rect(){
        int y = (int) Math.round(ypos);
        rect = new Rectangle(xpos, y, 30,30);
    }

    public boolean move(Boba b, Boba b1, double i){
        ypos += i;
/*        if (! (b.xpos >= 800 && b1.xpos <= 50)) {
            if (b.xpos >= 800) {
                xpos -= 1.5;
                most_recent = false;
            } else if (!most_recent) {
                xpos -= 1.5;
            } else {
                xpos += 1.5;
            }
            if (b1.xpos <= 50) {
                most_recent = true;
            }
        }*/
        build_rect();
        return true;
    }
}
