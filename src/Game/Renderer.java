package Game;



import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Renderer implements GLEventListener {

    private Texture image;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glClearColor(0f, 0f, 0f, 1f);

        try {
            // تحميل الصورة من package assets
            java.net.URL url = getClass().getClassLoader().getResource("assets/back_1.png");
            if (url != null) {
                image = TextureIO.newTexture(url, false, TextureIO.PNG);
            } else {
                System.out.println("Image not found in assets!");
            }
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        int w = drawable.getWidth();
        int h = drawable.getHeight();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // إعداد 2D ortho
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();


        if (image != null) {
            image.bind();
            gl.glBegin(GL.GL_QUADS);

            gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(w, 0);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(w, h);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(0, h);

            gl.glEnd();
        }

    }

    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().glViewport(0, 0, w, h);
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }


    public void dispose(GLAutoDrawable drawable) {}
}
