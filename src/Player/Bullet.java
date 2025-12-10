package Player;

import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import texture.constants;

public class Bullet {
    private float x, y;
    private int width, height;
    private float speed;
    private Texture texture;
    private boolean active;

    public Bullet(String imagePath, float startX, float startY, int width, int height) {
        this.width = width;
        this.height = height;
        this.speed = 10;
        this.x = startX;
        this.y = startY;
        this.active = true;

        try {
            texture = TextureIO.newTexture(new File(imagePath), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update() {
        if (!active) return;
        y += speed;
        if (y > constants.WINDOW_HEIGHT)
            active = false;
    }

    public void draw(GL gl) {
        if (!active || texture == null) return;

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        texture.enable();
        texture.bind();

        float x1 = 2.0f * x / constants.WINDOW_WIDTH - 1.0f;
        float y1 = 2.0f * y / constants.WINDOW_HEIGHT - 1.0f;
        float x2 = 2.0f * (x + width) / constants.WINDOW_WIDTH - 1.0f;
        float y2 = 2.0f * (y + height) / constants.WINDOW_HEIGHT - 1.0f;

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(x1, y1);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(x2, y1);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(x2, y2);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(x1, y2);
        gl.glEnd();

        texture.disable();
        gl.glDisable(GL.GL_BLEND);
    }
    public boolean isActive() {
        return active;
    }
}
