package Player;

import Enemies.rockManager;
import com.sun.opengl.util.GLUT;
import Enemies.ChickenManager;
import javax.media.opengl.*;
import Enemies.Chickens;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.util.List;
import Game.GameManager;
import texture.constants;

public class GameTestCanvas extends GLCanvas implements GLEventListener {

    private int level    = 0;
    private SpaceShip ship;
    private Controls controls;
    private com.sun.opengl.util.FPSAnimator animator;
    private ChickenManager chickenManager;
    private rockManager rocks;
    private int score = 0    ;
    private GLUT glut = new GLUT();

    private Texture background;
    private final GameManager gameManager;
    private Texture heartTexture;
    private Texture gameOverTexture;
    private Texture winTexture;
    public GameTestCanvas(GameManager gameManager, SpaceShip ship, Controls controls ) {
        this.gameManager = gameManager;
        this.ship = ship;
        this.controls = controls;

        chickenManager = new ChickenManager();
rocks=new rockManager();

        addGLEventListener(this);
    }

    public void setLevel(int lvl) {
        this.level = lvl;

        chickenManager = new ChickenManager();
        chickenManager.level(lvl);

        if (ship != null) {
            ship.setBulletsCount(lvl);
        }

        controls.level = lvl;

        score = 0;
    }


    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();


        try {
            background = TextureIO.newTexture(
                    new File("src/assets/back2.png"),
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            gameOverTexture = TextureIO.newTexture(
                    new File("src/assets/gameover.png"),
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            winTexture = TextureIO.newTexture(
                    new File("src/assets/win.png"),
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        ship.init(gl);
        chickenManager.init(drawable);
        rocks.init(drawable);
        try {
            heartTexture = TextureIO.newTexture(
                    new File("src/assets/heart.png"), true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        gl.glClearColor(0f, 0f, 0f, 1f);

        animator = new FPSAnimator(drawable, 60);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();


        if (level <= 0) {
            return;
        }

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        if (background != null) {
            background.enable();
            background.bind();

            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0f, 0f);
            gl.glVertex2f(-1f, -1f);
            gl.glTexCoord2f(1f, 0f);
            gl.glVertex2f(1f, -1f);
            gl.glTexCoord2f(1f, 1f);
            gl.glVertex2f(1f, 1f);
            gl.glTexCoord2f(0f, 1f);
            gl.glVertex2f(-1f, 1f);
            gl.glEnd();

            background.disable();


            controls.handleKeyPress(ship);
            gl.glEnable(GL.GL_TEXTURE_2D);
            chickenManager.display(drawable);
rocks.display(drawable);
            List<Bullet> bullets = controls.bullets;
            for (int bi = 0; bi < bullets.size(); bi++) {
                Bullet b = bullets.get(bi);
                if (b.isActive()) {
                    b.update();
                    b.draw(gl);
                }
            }
            bullets.removeIf(b -> !b.isActive());

            List<Chickens> chickens = chickenManager.getChickens();
            if (chickens == null || chickens.isEmpty()) {
                drawWin(gl);
                return;
            }

            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);

                float bx1 = b.getX();
                float by1 = b.getY();
                float bx2 = bx1 + b.getWidth();
                float by2 = by1 + b.getHeight();

                for (int j = 0; j < chickens.size(); j++) {
                    Chickens c = chickens.get(j);

                    float cx1 = (c.x + 10) * 50;
                    float cy1 = (c.y + 10) * 50;
                    float cx2 = cx1 + 100;
                    float cy2 = cy1 + 100;

                    boolean collision =
                            bx1 < cx2 && bx2 > cx1 &&
                                    by1 < cy2 && by2 > cy1;

                    if (collision) {
                        chickens.remove(j);
                        bullets.remove(i);
                        score += 5;
                        gameManager.setCurrentScore(score);
                        System.out.println("Score = " + score);
                        i--;
                        break;
                    }
                }
            }

            int[] rx = rocks.getRockX();
            int[] ry = rocks.getRockY();

            float shipX = ship.getX();
            float shipY = ship.getY();

            int shipW = ship.getWidth();
            int shipH = ship.getHeight();

            for (int i = 0; i < rx.length; i++) {

                int rockW = 40;
                int rockH = 40;

                boolean collision =
                        shipX < rx[i] + rockW &&
                                shipX + shipW > rx[i] &&
                                shipY < ry[i] + rockH &&
                                shipY + shipH > ry[i];

                if (collision) {

                    ship.addHit();
                    System.out.println("Ship hit! Hits = " + ship.getHits());

                    ry[i] = constants.FRAME_HEIGHT + 100;


                    if (ship.getHits() >= 3) {
                        ship.kill();
                    }
                    break;
                }
            }
            if (ship.isAlive()) {
                ship.draw(gl);
            }
            if (!ship.isAlive()) {
                drawGameOver(gl);
                return;
            }

            gl.glDisable(GL.GL_TEXTURE_2D);
            gl.glColor3f(1f, 1f, 1f);
            gl.glRasterPos2f(-0.95f, 0.9f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Score: " + score);
            gl.glEnable(GL.GL_TEXTURE_2D);
        }
        drawHearts(gl);
    }
    private void drawHearts(GL gl) {
        if (heartTexture == null) return;

        heartTexture.enable();
        heartTexture.bind();

        int heartsLeft = 3 - ship.getHits();

        float x = -0.95f;
        float y = 0.85f;
        float size = 0.1f;

        for (int i = 0; i < heartsLeft; i++) {
            gl.glBegin(GL.GL_QUADS);

            gl.glTexCoord2f(0, 1); gl.glVertex2f(x, y);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(x + size, y);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(x + size, y + size);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(x, y + size);

            gl.glEnd();

            x += size + 0.05f;
        }

        heartTexture.disable();
    }
    private void drawGameOver(GL gl) {
        if (gameOverTexture == null) return;

        gameOverTexture.enable();
        gameOverTexture.bind();

        float w = 0.6f;
        float h = 0.3f;

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0, 1); gl.glVertex2f(-w/2, -h/2);
        gl.glTexCoord2f(1, 1); gl.glVertex2f( w/2, -h/2);
        gl.glTexCoord2f(1, 0); gl.glVertex2f( w/2,  h/2);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(-w/2,  h/2);

        gl.glEnd();

        gameOverTexture.disable();
    }
    private void drawWin(GL gl) {
        if (winTexture == null) return;

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        winTexture.enable();
        winTexture.bind();

        float w = 0.6f;
        float h = 0.3f;

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0, 1); gl.glVertex2f(-w/2, -h/2);
        gl.glTexCoord2f(1, 1); gl.glVertex2f( w/2, -h/2);
        gl.glTexCoord2f(1, 0); gl.glVertex2f( w/2,  h/2);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(-w/2,  h/2);

        gl.glEnd();

        winTexture.disable();
        gl.glDisable(GL.GL_BLEND);
    }


    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {}
}
