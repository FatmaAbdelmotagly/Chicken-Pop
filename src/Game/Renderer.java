package Game;

import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;

public class Renderer implements GLEventListener, MouseListener {

    // Game States
    private final int STATE_MENU = 0;
    private final int STATE_LEVEL_SELECT = 1;
    private final int STATE_GAME_PLAY = 2;

    private int gameState = STATE_MENU;

    // Textures الرئيسية
    private Texture background;
    private Texture levelBackground;
    private Texture btnStart, btnExit;
    private int startX, startY, startW, startH;
    private int exitX, exitY, exitW, exitH;

    // Textures Levels
    private Texture level1Tex, level2Tex, level3Tex;
    private int level1X, level1Y, level2X, level2Y, level3X, level3Y;
    private int levelW, levelH;

    // Back Button
    private Texture btnBack;
    private int backX, backY, backW, backH;

    // Background لكل مستوى
    private Texture level1BgTex, level2BgTex, level3BgTex;
    private Texture currentBgTex;

    private GLCanvas canvas;

    // Sounds
    private Sound clickSound;
    private Sound gameMusic;

    public Renderer(GLCanvas canvas){
        this.canvas = canvas;
        canvas.addMouseListener(this);
    }

    public void init(GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glClearColor(0f,0f,0f,1f);

        try {
            // القائمة الرئيسية
            URL bgURL = getClass().getClassLoader().getResource("assets/back_1.png");
            if(bgURL != null) background = TextureIO.newTexture(bgURL,false,TextureIO.PNG);

            URL levelBgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(levelBgURL != null) levelBackground = TextureIO.newTexture(levelBgURL,false,TextureIO.PNG);

            URL startURL = getClass().getClassLoader().getResource("assets/sst.png");
            if(startURL != null) btnStart = TextureIO.newTexture(startURL,false,TextureIO.PNG);

            URL exitURL = getClass().getClassLoader().getResource("assets/eex.png");
            if(exitURL != null) btnExit = TextureIO.newTexture(exitURL,false,TextureIO.PNG);

            // Levels
            URL l1URL = getClass().getClassLoader().getResource("assets/lvl1.png");
            if(l1URL != null) level1Tex = TextureIO.newTexture(l1URL,false,TextureIO.PNG);

            URL l2URL = getClass().getClassLoader().getResource("assets/lvl2.png");
            if(l2URL != null) level2Tex = TextureIO.newTexture(l2URL,false,TextureIO.PNG);

            URL l3URL = getClass().getClassLoader().getResource("assets/lvl3.png");
            if(l3URL != null) level3Tex = TextureIO.newTexture(l3URL,false,TextureIO.PNG);

            // Back Button
            // تم تغيير اسم الملف إلى "bbb.jpg" أو "bb.png" بناءً على آخر كود أرسلتيه
            URL backURL = getClass().getClassLoader().getResource("assets/bb.png");
            if(backURL != null) btnBack = TextureIO.newTexture(backURL,false,TextureIO.PNG);

            // Level Backgrounds (مفترض أنك ستغيرين هذه الصور لاحقاً)
            URL l1BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l1BgURL != null) level1BgTex = TextureIO.newTexture(l1BgURL,false,TextureIO.PNG);

            URL l2BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l2BgURL != null) level2BgTex = TextureIO.newTexture(l2BgURL,false,TextureIO.PNG);

            URL l3BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l3BgURL != null) level3BgTex = TextureIO.newTexture(l3BgURL,false,TextureIO.PNG);

            // Sounds
            clickSound = new Sound("click.wav");
            gameMusic = new Sound("game sound.wav");
            if(gameMusic != null) gameMusic.loop();

        } catch(Exception e){
            System.err.println("Failed to load resources: " + e.getMessage());
        }

        int canvasW = drawable.getWidth();
        int canvasH = drawable.getHeight();

        // Menu Buttons
        startW = exitW = 300;
        startH = exitH = 150;
        int spacing = 30;
        startX = (canvasW - startW)/2;
        exitX  = startX;
        startY = (canvasH/2) - (startH/2) - 50;
        exitY  = startY - exitH - spacing;

        // Level Buttons
        levelW = 350;
        levelH = 130;
        int padding = 30;
        int levelX = (canvasW - levelW)/2;
        int totalHeight = levelH*3 + padding*2;
        int startYPos = (canvasH/2) - (totalHeight/2);

        level3X = levelX; level3Y = startYPos;
        level2X = levelX; level2Y = startYPos + levelH + padding;
        level1X = levelX; level1Y = startYPos + (levelH*2) + (padding*2);

        // Back Button
        // تم ضبط هذه القيم لتكون أعلى في الشاشة
        backW = 150; backH = 70;
        backX = 20; backY = canvasH - backH - 20; // هذا يضع الزر في الزاوية العلوية اليسرى
    }

    // *** دالة جديدة لرسم زر العودة ***
    private void drawBackButton(GL gl) {
        if(btnBack != null){
            btnBack.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(backX,backY);
            gl.glTexCoord2f(1,1); gl.glVertex2f(backX+backW,backY);
            gl.glTexCoord2f(1,0); gl.glVertex2f(backX+backW,backY+backH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(backX,backY+backH);
            gl.glEnd();
        }
    }


    public void display(GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        int w = drawable.getWidth();
        int h = drawable.getHeight();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        Texture currentBackground = null;

        if(gameState == STATE_MENU) currentBackground = background;
        else if(gameState == STATE_LEVEL_SELECT) currentBackground = levelBackground;
        else if(gameState == STATE_GAME_PLAY) currentBackground = currentBgTex;

        if(currentBackground != null){
            currentBackground.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(0,0);
            gl.glTexCoord2f(1,1); gl.glVertex2f(w,0);
            gl.glTexCoord2f(1,0); gl.glVertex2f(w,h);
            gl.glTexCoord2f(0,0); gl.glVertex2f(0,h);
            gl.glEnd();
        }

        if(gameState == STATE_MENU) drawMainMenu(gl);
        else if(gameState == STATE_LEVEL_SELECT) drawLevelSelection(gl);
            // *** رسم زر العودة في شاشة اللعب ***
        else if(gameState == STATE_GAME_PLAY) {
            // لا نرسم أي شيء هنا غير الخلفية (وهي مرسومة بالفعل)
            drawBackButton(gl); // رسم زر العودة فقط
        }
    }

    private void drawMainMenu(GL gl){
        if(btnStart != null){
            btnStart.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(startX,startY);
            gl.glTexCoord2f(1,1); gl.glVertex2f(startX+startW,startY);
            gl.glTexCoord2f(1,0); gl.glVertex2f(startX+startW,startY+startH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(startX,startY+startH);
            gl.glEnd();
        }
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

    private void drawLevelSelection(GL gl){
        if(level1Tex != null){
            level1Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level1X,level1Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level1X+levelW,level1Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level1X+levelW,level1Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level1X,level1Y+levelH);
            gl.glEnd();
        }
        if(level2Tex != null){
            level2Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level2X,level2Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level2X+levelW,level2Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level2X+levelW,level2Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level2X,level2Y+levelH);
            gl.glEnd();
        }
        if(level3Tex != null){
            level3Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level3X,level3Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level3X+levelW,level3Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level3X+levelW,level3Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level3X,level3Y+levelH);
            gl.glEnd();
        }

        // *** رسم زر العودة في شاشة اختيار المستويات ***
        drawBackButton(gl);
    }

    public void reshape(GLAutoDrawable d, int x, int y, int w, int h){
        d.getGL().glViewport(0,0,w,h);
    }
    public void dispose(GLAutoDrawable drawable){}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged){}

    public void mouseClicked(MouseEvent e){
        int my = canvas.getHeight() - e.getY();
        int mx = e.getX();

        if(gameState == STATE_MENU){
            if(mx >= startX && mx <= startX+startW && my >= startY && my <= startY+startH){
                if(clickSound != null) clickSound.play();
                gameState = STATE_LEVEL_SELECT;
                canvas.repaint();
            } else if(mx >= exitX && mx <= exitX+exitW && my >= exitY && my <= exitY+exitH){
                if(clickSound != null) clickSound.play();
                if(gameMusic != null) gameMusic.stop();
                System.exit(0);
            }
        } else if(gameState == STATE_LEVEL_SELECT){
            // Back to Menu
            if(mx >= backX && mx <= backX+backW && my >= backY && my <= backY+backH){
                if(clickSound != null) clickSound.play();
                gameState = STATE_MENU;
                canvas.repaint();
                return;
            }

            // Levels
            if(mx >= level1X && mx <= level1X+levelW && my >= level1Y && my <= level1Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level1BgTex;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            } else if(mx >= level2X && mx <= level2X+levelW && my >= level2Y && my <= level2Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level2BgTex;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            } else if(mx >= level3X && mx <= level3X+levelW && my >= level3Y && my <= level3Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level3BgTex;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            }
        }
        // *** منطق النقر في حالة اللعب: العودة إلى شاشة اختيار المستويات ***
        else if (gameState == STATE_GAME_PLAY) {
            if(mx >= backX && mx <= backX+backW && my >= backY && my <= backY+backH){
                if(clickSound != null) clickSound.play();
                gameState = STATE_LEVEL_SELECT; // العودة من اللعب إلى اختيار المستويات
                canvas.repaint();
            }
        }
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}