package Game;

import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;

public class Renderer implements GLEventListener, MouseListener {

    // *** حالات اللعبة (Game States) ***
    private final int STATE_MENU = 0;
    private final int STATE_LEVEL_SELECT = 1;
    private final int STATE_GAME_PLAY = 2;

    private int gameState = STATE_MENU;

    // *** الأنسجة والمتغيرات الخاصة بالقائمة الرئيسية ***
    private Texture background;
    private Texture levelBackground;
    private Texture btnStart, btnExit;
    private int startX, startY, startW, startH;
    private int exitX, exitY, exitW, exitH;

    // *** الأنسجة والمتغيرات الخاصة باختيار المستويات ***
    private Texture level1Tex, level2Tex, level3Tex;
    private int level1X, level1Y, levelW, levelH;
    private int level2X, level2Y;
    private int level3X, level3Y;

    private GLCanvas canvas;

    // *** كائنات الصوت ***
    private Sound clickSound;
    private Sound gameMusic;

    public Renderer(GLCanvas canvas) {
        this.canvas = canvas;
        canvas.addMouseListener(this);
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glClearColor(0f, 0f, 0f, 1f);

        // تحميل الأنسجة والأصوات
        try {
            // تحميل القائمة الرئيسية
            URL bgURL = getClass().getClassLoader().getResource("assets/back_1.png");
            if(bgURL != null) background = TextureIO.newTexture(bgURL, false, TextureIO.PNG);

            // تحميل الخلفية الجديدة
            URL levelBgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(levelBgURL != null) levelBackground = TextureIO.newTexture(levelBgURL, false, TextureIO.PNG);

            URL startURL = getClass().getClassLoader().getResource("assets/sst.png");
            if(startURL != null) btnStart = TextureIO.newTexture(startURL, false, TextureIO.PNG);

            URL exitURL = getClass().getClassLoader().getResource("assets/eex.png");
            if(exitURL != null) btnExit = TextureIO.newTexture(exitURL, false, TextureIO.PNG);

            // تحميل صور المستويات
            URL l1URL = getClass().getClassLoader().getResource("assets/lvl1.png");
            if(l1URL != null) level1Tex = TextureIO.newTexture(l1URL, false, TextureIO.PNG);

            URL l2URL = getClass().getClassLoader().getResource("assets/lvl2.png");
            if(l2URL != null) level2Tex = TextureIO.newTexture(l2URL, false, TextureIO.PNG);

            URL l3URL = getClass().getClassLoader().getResource("assets/lvl3.png");
            if(l3URL != null) level3Tex = TextureIO.newTexture(l3URL, false, TextureIO.PNG);

            // تحميل وتهيئة كائنات الصوت
            clickSound = new Sound("click.wav");
            gameMusic = new Sound("game sound.wav");

            if(gameMusic != null) {
                gameMusic.loop();
            }

        } catch(Exception e){
            System.err.println("Failed to load resources: " + e.getMessage());
        }

        int canvasW = drawable.getWidth();
        int canvasH = drawable.getHeight();

        // 1. تحديد مواقع القائمة الرئيسية (Menu Positioning)
        startW = 300; startH = 150;
        exitW  = 300; exitH  = 150;
        int spacing = 30;

        startX = (canvasW - startW) / 2;
        exitX  = (canvasW - exitW) / 2;
        startY = (canvasH / 2) - (startH / 2) - 50;
        exitY  = startY - exitH - spacing;

        // 2. تحديد مواقع اختيار المستويات (Level Selection Positioning)

        // *** الأبعاد المُعدلة (350x130) لضمان منطقة نقر جيدة ***
        levelW = 350;
        levelH = 130; // تم التعديل إلى 130
        int padding = 30;

        int levelX = (canvasW - levelW) / 2; // التوسيط الأفقي

        // التوسيط العمودي
        int totalHeight = (levelH * 3) + (padding * 2);
        int startYPos = (canvasH / 2) - (totalHeight / 2);

        // حساب مواضع Y (الترتيب: المستوى 1 في الأعلى)

        // المستوى 3 (الأسفل)
        level3X = levelX;
        level3Y = startYPos;

        // المستوى 2 (الأوسط)
        level2X = levelX;
        level2Y = startYPos + levelH + padding;

        // المستوى 1 (الأعلى)
        level1X = levelX;
        level1Y = startYPos + (levelH * 2) + (padding * 2);
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

        // منطق اختيار الخلفية المناسبة
        Texture currentBackground = null;

        if (gameState == STATE_MENU) {
            currentBackground = background;
        } else if (gameState == STATE_LEVEL_SELECT) {
            currentBackground = levelBackground;
        } else {
            currentBackground = background;
        }

        // رسم الخلفية المختارة
        if(currentBackground != null){
            currentBackground.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(0,0);
            gl.glTexCoord2f(1,1); gl.glVertex2f(w,0);
            gl.glTexCoord2f(1,0); gl.glVertex2f(w,h);
            gl.glTexCoord2f(0,0); gl.glVertex2f(0,h);
            gl.glEnd();
        }

        // رسم المحتوى بناءً على حالة اللعبة
        if (gameState == STATE_MENU) {
            drawMainMenu(gl);
        } else if (gameState == STATE_LEVEL_SELECT) {
            drawLevelSelection(gl);
        }
    }

    private void drawMainMenu(GL gl) {
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

    private void drawLevelSelection(GL gl) {
        // رسم Level 1
        if(level1Tex != null){
            level1Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level1X,level1Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level1X+levelW,level1Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level1X+levelW,level1Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level1X,level1Y+levelH);
            gl.glEnd();
        }

        // رسم Level 2
        if(level2Tex != null){
            level2Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level2X,level2Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level2X+levelW,level2Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level2X+levelW,level2Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level2X,level2Y+levelH);
            gl.glEnd();
        }

        // رسم Level 3
        if(level3Tex != null){
            level3Tex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0,1); gl.glVertex2f(level3X,level3Y);
            gl.glTexCoord2f(1,1); gl.glVertex2f(level3X+levelW,level3Y);
            gl.glTexCoord2f(1,0); gl.glVertex2f(level3X+levelW,level3Y+levelH);
            gl.glTexCoord2f(0,0); gl.glVertex2f(level3X,level3Y+levelH);
            gl.glEnd();
        }
    }


    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
        d.getGL().glViewport(0,0,w,h);
    }

    public void dispose(GLAutoDrawable drawable) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    // كشف ضغط الماوس (التحكم في منطق الانتقال بين الحالات)
    public void mouseClicked(MouseEvent e) {
        // تحويل إحداثيات الماوس: (0,0) في الأسفل لـ OpenGL
        int my = canvas.getHeight() - e.getY();
        int mx = e.getX();

        if (gameState == STATE_MENU) {

            // النقر على Start
            if(mx >= startX && mx <= startX + startW && my >= startY && my <= startY + startH){
                if (clickSound != null) clickSound.play();

                gameState = STATE_LEVEL_SELECT;
                System.out.println("Transition to Level Selection");

                canvas.repaint();

            }

            // النقر على Exit
            else if(mx >= exitX && mx <= exitX + exitW && my >= exitY && my <= exitY + exitH){
                if (clickSound != null) clickSound.play();
                if (gameMusic != null) gameMusic.stop();
                System.out.println("Exit clicked!");
                try { Thread.sleep(100); } catch (InterruptedException ex) {}
                System.exit(0);
            }
        }

        else if (gameState == STATE_LEVEL_SELECT) {

            // Level 1
            if(mx >= level1X && mx <= level1X + levelW && my >= level1Y && my <= level1Y + levelH){
                if (clickSound != null) clickSound.play();
                gameState = STATE_GAME_PLAY;
                System.out.println("Starting Level 1!");
                // **هنا ستضعي كود بدء اللعب للمستوى 1**
            }

            // Level 2
            else if(mx >= level2X && mx <= level2X + levelW && my >= level2Y && my <= level2Y + levelH){
                if (clickSound != null) clickSound.play();
                gameState = STATE_GAME_PLAY;
                System.out.println("Starting Level 2!");
                // **هنا ستضعي كود بدء اللعب للمستوى 2**
            }

            // Level 3
            else if(mx >= level3X && mx <= level3X + levelW && my >= level3Y && my <= level3Y + levelH){
                if (clickSound != null) clickSound.play();
                gameState = STATE_GAME_PLAY;
                System.out.println("Starting Level 3!");
                // **هنا ستضعي كود بدء اللعب للمستوى 3**
            }
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}