package Game;

import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;

public class Renderer implements GLEventListener, MouseListener {

    private Texture background;
    private Texture btnStart, btnExit;

    private int startX, startY, startW, startH;
    private int exitX, exitY, exitW, exitH;

    private GLCanvas canvas;

    public Renderer(GLCanvas canvas) {
        this.canvas = canvas;
        canvas.addMouseListener(this);
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glClearColor(0f, 0f, 0f, 1f);

        try {
            URL bgURL = getClass().getClassLoader().getResource("assets/back_1.png");
            if(bgURL != null) background = TextureIO.newTexture(bgURL, false, TextureIO.PNG);

            URL startURL = getClass().getClassLoader().getResource("assets/sst.png");
            if(startURL != null) btnStart = TextureIO.newTexture(startURL, false, TextureIO.PNG);

            URL exitURL = getClass().getClassLoader().getResource("assets/eex.png");
            if(exitURL != null) btnExit = TextureIO.newTexture(exitURL, false, TextureIO.PNG);
        } catch(Exception e){
            System.out.println("Failed to load images: " + e.getMessage());
        }

        // أبعاد الزرار
        startW = 300; startH = 150;
        exitW  = 300; exitH  = 150;

        int canvasW = drawable.getWidth();
        int canvasH = drawable.getHeight();


        int spacing = 20; // المسافة بين Start و Exit

        // متمركزين أفقيًا
        startX = (canvasW - startW) / 2;
        exitX  = (canvasW - exitW) / 2;


        startY = (canvasH / 2) - (startH / 2) - 50;
        exitY  = startY - exitH - spacing;    // Exit تحت Start بمسافة 20px

    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        int w = drawable.getWidth();
        int h = drawable.getHeight();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // رسم الخلفية
        if(background != null){
            background.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(0,0);
            gl.glTexCoord2f(1,1); gl.glVertex2f(w,0);
            gl.glTexCoord2f(1,0); gl.glVertex2f(w,h);
            gl.glTexCoord2f(0,0); gl.glVertex2f(0,h);
            gl.glEnd();
        }

        // رسم زر Start
        if(btnStart != null){
            btnStart.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(startX,startY);
            gl.glTexCoord2f(1,1); gl.glVertex2f(startX+startW,startY);
            gl.glTexCoord2f(1,0); gl.glVertex2f(startX+startW,startY+startH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(startX,startY+startH);
            gl.glEnd();
        }

        // رسم زر Exit
        if(btnExit != null){
            btnExit.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(exitX,exitY);
            gl.glTexCoord2f(1,1); gl.glVertex2f(exitX+exitW,exitY);
            gl.glTexCoord2f(1,0); gl.glVertex2f(exitX+exitW,exitY+exitH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(exitX,exitY+exitH);
            gl.glEnd();
        }
    }

    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().glViewport(0,0,w,h);
    }

    public void dispose(GLAutoDrawable drawable) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    // كشف ضغط الماوس
    public void mouseClicked(MouseEvent e) {
        int my = canvas.getHeight() - e.getY(); // flip Y
        int mx = e.getX();

        if(mx >= startX && mx <= startX + startW && my >= startY && my <= startY + startH){
            System.out.println("Start clicked!");
        }

        if(mx >= exitX && mx <= exitX + exitW && my >= exitY && my <= exitY + exitH){
            System.out.println("Exit clicked!");
            System.exit(0);
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
