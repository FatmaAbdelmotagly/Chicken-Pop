package Player;

import javax.media.opengl.*;
import com.sun.opengl.util.FPSAnimator;

public class GameCanvas implements GLEventListener {

    private SpaceShip ship;
    private Controls controls;
    private FPSAnimator animator;

    public GameCanvas(SpaceShip ship, Controls controls) {
        this.ship = ship;
        this.controls = controls;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        ship.init(gl);
        gl.glClearColor(0f, 0f, 0f, 1f);
        animator = new FPSAnimator(drawable, 60);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        controls.handleKeyPress(ship);
        ship.draw(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {

    }
}
