package texture;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import texture.constants;
import javax.media.opengl.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Background implements GLEventListener {

    private Texture texture;

    public Background() {
        JFrame frame = new JFrame("Background Frame");
        frame.setSize(constants.FRAME_WIDTH, constants.FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(this);

        frame.add(canvas);
        frame.setVisible(true);

        Animator animator = new Animator(canvas);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);

        try {
            texture = TextureIO.newTexture(
                    new File(constants.BACKGROUND_PATH), true
            );
        } catch (IOException e) {
            System.out.println("ERROR loading background image");
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        if (texture == null) return;

        texture.enable();
        texture.bind();

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0,0); gl.glVertex2f(-1,-1);
        gl.glTexCoord2f(1,0); gl.glVertex2f( 1,-1);
        gl.glTexCoord2f(1,1); gl.glVertex2f( 1, 1);
        gl.glTexCoord2f(0,1); gl.glVertex2f(-1, 1);

        gl.glEnd();
        texture.disable();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL gl = drawable.getGL();

        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -1, 1);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean b, boolean b1) {}
}
