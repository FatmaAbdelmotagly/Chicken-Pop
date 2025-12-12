package Player;

import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import texture.constants;

public class SpaceShip {
    private float x, y;
    private int width, height;
    private Texture texture;
    private String imagePath;
    private int bulletsCount = 1;
    private int hits = 0;
    boolean alive = true;

    public int getHits() { return hits; }
    public void addHit() { hits++; }

    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
    public void setX (int x){this.x=x; }
    public void setY (int y){this.y=y; }


    public SpaceShip(String imagePath) {
        this.width = constants.PLAYER_WIDTH;
        this.height = constants.PLAYER_HEIGHT;
        this.x = constants.WINDOW_WIDTH / 2 - width / 2;
        this.y = 50;
        this.imagePath = imagePath;
    }

    public void init(GL gl) {
        try {
            texture = TextureIO.newTexture(new File(imagePath), true);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(GL gl) {
        if (texture == null) return;
        if (!alive) return;

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

    public void moveLeft() {
        x -= constants.PLAYER_SPEED;
        if(x < 0)
            x = 0;
    }
    public void moveRight() {
        x += constants.PLAYER_SPEED;
        if(x + width > constants.WINDOW_WIDTH)
            x = constants.WINDOW_WIDTH - width;
    }
    public void moveUp() {
        y += constants.PLAYER_SPEED;
        if(y + height > constants.WINDOW_HEIGHT)
            y = constants.WINDOW_HEIGHT - height;
    }
    public void moveDown() {
        y -= constants.PLAYER_SPEED;
        if(y < 0)
            y = 0;
    }


    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height ;
    }

    public void setBulletsCount(int level) {
        if (level <= 0) level = 1;
        if (level == 1) bulletsCount = 1;
        else if (level == 2) bulletsCount = 2;
        else bulletsCount = 3;
    }

    public int getBulletsCount() {
        return bulletsCount;
    }
}