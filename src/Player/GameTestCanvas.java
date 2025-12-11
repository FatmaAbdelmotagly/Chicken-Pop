package Player;
import com.sun.opengl.util.GLUT;

import Enemies.ChickenManager;
import javax.media.opengl.*;

import Enemies.Chickens;
import com.sun.opengl.util.FPSAnimator;
import java.util.List;

public class GameTestCanvas extends GLCanvas implements GLEventListener {

    private SpaceShip ship;
    private Controls controls;
    private FPSAnimator animator;
    private ChickenManager chickenManager;
    private int score = 0;
    private GLUT glut = new GLUT();


    public GameTestCanvas(SpaceShip ship, Controls controls) {
        this.ship = ship;
        this.controls = controls;

        chickenManager = new ChickenManager();
        chickenManager.level(1);

        addGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        ship.init(gl);
        chickenManager.init(drawable);

        gl.glClearColor(0f, 0f, 0f, 1f);

        animator = new FPSAnimator(drawable, 60);
        animator.start();
    }
    @Override
    public void display(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        controls.handleKeyPress(ship);
        gl.glEnable(GL.GL_TEXTURE_2D);
        chickenManager.display(drawable);

        List<Bullet> bullets = controls.bullets;
        for (Bullet b : bullets) {
            if (b.isActive()) {
                b.update();
                b.draw(gl);
            }
        }
        bullets.removeIf(b -> !b.isActive());

        List<Chickens> chickens = chickenManager.getChickens();

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
                    System.out.println("Score = " + score);
                    i--;
                    break;
                }
            }
        }


        ship.draw(gl);

        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1f, 1f, 1f);
        gl.glRasterPos2f(-0.95f, 0.9f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Score: " + score);
        gl.glEnable(GL.GL_TEXTURE_2D);

    }


    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().glViewport(0, 0, w, h);
    }

    @Override
    public void displayChanged(GLAutoDrawable d, boolean m, boolean d2) {}


}
