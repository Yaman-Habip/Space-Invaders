import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Random;

public class Setup implements Runnable, KeyListener {

    final int WIDTH = 1000;
    final int HEIGHT = 700;

    public int score = 0;

    public int high_score = 0;

    public boolean new_high_score = false;


    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    public Image ship;
    public Image enemy;
    public Image bullet;
    public Image background;
    public Image game_over;

    public GoodGuy me = new GoodGuy();
    public ArrayList<Bullet> all_bullets = new ArrayList<Bullet>();

    public ArrayList<ArrayList<BadGuy>> all_badguys = new ArrayList<ArrayList<BadGuy>>();
    public double difficulty = 1;
    public int up_difficulty = 8000;

    public String game_state = "Boba";
    public int state = 0;


    public Setup() {
        setUpGraphics();

        ship = Toolkit.getDefaultToolkit().getImage("good_guy.jpeg");
        enemy = Toolkit.getDefaultToolkit().getImage("the_enemy.jpeg");
        bullet = Toolkit.getDefaultToolkit().getImage("bullet.jpeg");
        background = Toolkit.getDefaultToolkit().getImage("PixelMountain.jpg");
        game_over = Toolkit.getDefaultToolkit().getImage("game_over.jpeg");

        for (int i = 0; i < 7; i ++){
            all_badguys.add(new ArrayList<BadGuy>());
        }

        canvas.addKeyListener(this);
    }

    private void restart(){
        me.is_alive = true;
        new_high_score = false;
        for (ArrayList<BadGuy> i: all_badguys){
            i.clear();
        }
        all_badguys.clear();
        for (int i = 0; i < 7; i ++){
            all_badguys.add(new ArrayList<BadGuy>());
        }
        all_bullets.clear();
        me = new GoodGuy();
        difficulty = 1;
        up_difficulty = 8000;
        score = 0;
    }

    public static void main(String[] args) {
        Setup go = new Setup();
        new Thread(go).start();
    }

    public void run() {
        while (true) {
            while (me.is_alive) {
                move();
                check_intersections();
                draw();
                score++;
                if ((score >= up_difficulty) && (difficulty < 15)){
                    difficulty += 0.2;
                    up_difficulty = up_difficulty + 6000;
                }
                pause(20);
                state += 20;
                if (state % 3000 == 0){
                    if (game_state = "boba") {
                        game_state = "space";
                    }else{
                        game_state = "boba";
                    }
                }
            }
            while (! me.is_alive) {
                game_over();
                pause(20);
            }
        }
    }

    public void keyTyped (KeyEvent e){
        int key = e.getKeyCode();
        if ((key == 39 || key == 68) && me.xpos < 950) { // right key
            me.xpos = me.xpos + 30;
        } else if ((key == 37 || key == 65) && me.xpos > 0) { // left key
            me.xpos = me.xpos - 30;
        }
    }

    public void keyPressed (KeyEvent f){
        int key = f.getKeyCode();
        if ((key == 39 || key == 68) && me.xpos < 950) { // right key/ D key
            me.xpos = me.xpos + 30;
        } else if ((key == 37 || key == 65) && me.xpos > 0) { // left key/ A key
            me.xpos = me.xpos - 30;
        } else if (key == 32){ // space key
            if (me.is_alive) {
                Bullet k = new Bullet(me.xpos + 20, me.ypos - 10);
                all_bullets.add(k);
            } else{
                pause(200);
                restart();
            }
        }
    }

    public void keyReleased (KeyEvent keyEvent){;}

    private void move(){
        if (all_bullets.size() > 0) {
            for (int i = 0; i < all_bullets.size(); i++) {
                all_bullets.get(i).move();
                if (all_bullets.get(i).ypos <= 0) {
                    all_bullets.remove(i);
                }
            }
        }
        for (ArrayList<BadGuy> i: all_badguys){
            for (BadGuy j: i){
                if ( ! j.move(i.get(i.size() - 1), i.get(0), difficulty)){
                    me.is_alive = false;
                }
            }
        }
        if (all_badguys.get(0).isEmpty()){
            new_badguys();
        } else if (all_badguys.get(0).get(0).ypos >= 100){
            new_badguys();
        }
    }

    private void new_badguys(){
            Random pick_a_number = new Random();
            int random_number = pick_a_number.nextInt(9);
            Random pick_a_number2 = new Random();
            int random_number2 = pick_a_number2.nextInt(20);
            random_number2 = random_number2 + 40;
            random_number ++;
            all_badguys.remove(6);
            all_badguys.add(0, new ArrayList<BadGuy>());
            for (int i = 0; i < random_number; i++){
                if (random_number < 8) {
                    BadGuy k = new BadGuy(random_number2 + (i * 100), 20);
                    all_badguys.get(0).add(k);
                } else{
                    BadGuy k = new BadGuy(50 + (i * 100), 20);
                    all_badguys.get(0).add(k);
                }
            }
        }

    private void game_over(){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(game_over, 0, 0, WIDTH, HEIGHT, null);
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.PLAIN, 50));
        if (score > high_score){
            high_score = score;
            new_high_score = true;
        }
        if (new_high_score){
            g.drawString("New High Score!", 325, 250);
        }
        g.drawString("Press space to play again", 250, 500);
        g.setFont(new Font("arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 800, 650);
        g.drawString("High Score: " + high_score, 800, 675);
        g.dispose();
        bufferStrategy.show();
    }

    private void draw() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(ship, me.xpos, me.ypos, 50, 50, null);

        for (int i = 0; i < all_bullets.size(); i++){
            g.drawImage(bullet, all_bullets.get(i).xpos, all_bullets.get(i).ypos, 10, 10, null);
        }

        for (ArrayList<BadGuy> i: all_badguys){
            for (BadGuy j: i){
                int y = (int) Math.round(j.ypos);
                g.drawImage(enemy, j.xpos, y, 75,75,null);
            }
        }

        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 800, 650);
        g.drawString("High Score: " + high_score, 800, 675);
        g.dispose();
        bufferStrategy.show();
    }

    private void check_intersections() {
        ArrayList<Integer> bullets_to_remove = new ArrayList<Integer>();
        for (ArrayList<BadGuy> i : all_badguys) {
            ArrayList<Integer> badguys_to_remove = new ArrayList<>();
            for (Integer j = 0; j < i.size(); j++) {
                for (Integer k = 0; k < all_bullets.size(); k++) {
                    try {
                        if (i.get(j).rect.intersects(all_bullets.get(k).rect)) {
                            if (!bullets_to_remove.contains(k)) {
                                bullets_to_remove.add(k);
                                score = score + 100;
                            }
                            if (!badguys_to_remove.contains(j)) {
                                badguys_to_remove.add(j);
                            }
                        }
                    } catch (NullPointerException ignored) {;}
                }
            }
            for (int a : badguys_to_remove) {
                i.remove(a);
            }
        }
        for (int a : bullets_to_remove) {
            if (a < all_bullets.size()) {
                all_bullets.remove(a);
            }
        }
    }

    private void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {;}
    }

    private void setUpGraphics() {

        frame = new JFrame("Space Invaders");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
    }
}