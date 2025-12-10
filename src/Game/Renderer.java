package Game;

import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.net.URL;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;
import java.io.IOException;

public class Renderer implements GLEventListener, MouseListener {

    // Game States
    private final int STATE_MENU = 0;
    private final int STATE_LEVEL_SELECT = 1;
    private final int STATE_GAME_PLAY = 2;
    private final int STATE_PAUSE = 3;     // حالة الإيقاف المؤقت
    private final int STATE_GAME_OVER = 4; // حالة نهاية اللعبة

    private int gameState = STATE_MENU;
    private int currentLevel = 0; // 1, 2, or 3

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

    // HUD & Pause Menu & Game Over Textures
    private Texture btnPause;
    private int pauseX, pauseY, pauseW, pauseH;

    private Texture btnResume;
    private int resumeW, resumeH, resumeX, resumeY;

    private Texture lifeHeartTex;
    private int currentScore = 0;
    private int currentLives = 3;

    private Texture gameOverScreenTex;
    private Texture btnRetry;
    private int gameOverW, gameOverH, gameOverX, gameOverY;
    private int retryW, retryH, retryX, retryY;

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
        // تمكين الشفافية (مهم لقوائم التوقف/القلوب الشفافة)
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glClearColor(0f,0f,0f,1f);

        try {
            // القائمة الرئيسية والمستويات
            URL bgURL = getClass().getClassLoader().getResource("assets/back_1.png");
            if(bgURL != null) background = TextureIO.newTexture(bgURL,false,TextureIO.PNG);

            URL levelBgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(levelBgURL != null) levelBackground = TextureIO.newTexture(levelBgURL,false,TextureIO.PNG);

            URL startURL = getClass().getClassLoader().getResource("assets/sst.png");
            if(startURL != null) btnStart = TextureIO.newTexture(startURL,false,TextureIO.PNG);

            URL exitURL = getClass().getClassLoader().getResource("assets/eex.png");
            if(exitURL != null) btnExit = TextureIO.newTexture(exitURL,false,TextureIO.PNG);

            URL l1URL = getClass().getClassLoader().getResource("assets/lvl1.png");
            if(l1URL != null) level1Tex = TextureIO.newTexture(l1URL,false,TextureIO.PNG);

            URL l2URL = getClass().getClassLoader().getResource("assets/lvl2.png");
            if(l2URL != null) level2Tex = TextureIO.newTexture(l2URL,false,TextureIO.PNG);

            URL l3URL = getClass().getClassLoader().getResource("assets/lvl3.png");
            if(l3URL != null) level3Tex = TextureIO.newTexture(l3URL,false,TextureIO.PNG);

            // Back Button
            URL backURL = getClass().getClassLoader().getResource("assets/bb.png");
            if(backURL != null) btnBack = TextureIO.newTexture(backURL,false,TextureIO.PNG);

            // Level Backgrounds
            URL l1BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l1BgURL != null) level1BgTex = TextureIO.newTexture(l1BgURL,false,TextureIO.PNG);
            URL l2BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l2BgURL != null) level2BgTex = TextureIO.newTexture(l2BgURL,false,TextureIO.PNG);
            URL l3BgURL = getClass().getClassLoader().getResource("assets/back2.png");
            if(l3BgURL != null) level3BgTex = TextureIO.newTexture(l3BgURL,false,TextureIO.PNG);

            // HUD & Menu Textures (يجب توفير هذه الصور في مجلد assets)
            URL pauseURL = getClass().getClassLoader().getResource("assets/pause.png");
            if(pauseURL != null) btnPause = TextureIO.newTexture(pauseURL,false,TextureIO.PNG);

            URL heartURL = getClass().getClassLoader().getResource("assets/heart.png");
            if(heartURL != null) lifeHeartTex = TextureIO.newTexture(heartURL,false,TextureIO.PNG);

            URL resumeURL = getClass().getClassLoader().getResource("assets/resume.png");
            if(resumeURL != null) btnResume = TextureIO.newTexture(resumeURL,false,TextureIO.PNG);

            URL gameOverURL = getClass().getClassLoader().getResource("assets/gameover.png");
            if(gameOverURL != null) gameOverScreenTex = TextureIO.newTexture(gameOverURL,false,TextureIO.PNG);

            URL retryURL = getClass().getClassLoader().getResource("assets/retry.png");
            if(retryURL != null) btnRetry = TextureIO.newTexture(retryURL,false,TextureIO.PNG);


            // Sounds
            clickSound = new Sound("click.wav");
            gameMusic = new Sound("game sound.wav");
            if(gameMusic != null) gameMusic.loop();

        } catch(IOException e){
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
        backW = 150; backH = 70;
        backX = 20; backY = canvasH - backH - 20;

        // Pause Button (أعلى اليمين)
        pauseW = 150; pauseH =70;
        pauseX = canvasW - pauseW - 20;
        pauseY = canvasH - pauseH - 20;

        // Resume Button (منتصف الشاشة)
        resumeW = 200; resumeH = 80;
        resumeX = (canvasW - resumeW) / 2;
        resumeY = (canvasH / 2) - 50;

        // Game Over Screen
        gameOverW = 600; gameOverH = 400;
        gameOverX = (canvasW - gameOverW) / 2;
        gameOverY = (canvasH - gameOverH) / 2;

        // Retry Button
        retryW = 200; retryH = 80;
        retryX = (canvasW - retryW) / 2;
        retryY = gameOverY + 50;
    }

    // دالة مساعدة لرسم زر العودة
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

    // دالة مساعدة لعرض النص (يجب استبدالها بـ TextRenderer إذا أمكن)
    private void drawTextPlaceholder(GL gl, String text, int x, int y) {
        // نستخدم مربع ملون كـ Placeholder
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 0.0f); // لون أصفر للنقاط
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + 150, y);
        gl.glVertex2f(x + 150, y + 25);
        gl.glVertex2f(x, y + 25);
        gl.glEnd();
        gl.glColor3f(1.0f, 1.0f, 1.0f); // إعادة اللون للأبيض
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    // دالة رسم الـ HUD (النقاط والأرواح وزر الإيقاف المؤقت)
    private void drawHUD(GL gl, int w, int h) {
        // 1. زر الإيقاف المؤقت (Pause Button)
        if (btnPause != null) {
            // ... (كود زر الإيقاف المؤقت) ...
            btnPause.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(pauseX, pauseY);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(pauseX + pauseW, pauseY);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(pauseX + pauseW, pauseY + pauseH);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(pauseX, pauseY + pauseH);
            gl.glEnd();
        }

        // 3. Score (النقاط) - نضعه أولاً لتحديد موقع القلوب بعده
        // الـ Placeholder عرضه 150 + 20 (مسافة البداية) = 170
        int scorePlaceholderW = 150;
        int scorePlaceholderX = 20;
        int scorePlaceholderY = h - 50;

        drawTextPlaceholder(gl, "Score: " + currentScore, scorePlaceholderX, scorePlaceholderY);


        // 2. الأرواح (Lives) - رسم القلوب
        if (lifeHeartTex != null) {

            int heartSizeW =120;  // العرض
            int heartSizeH = 55;  // الارتفاع

// المسافة بين القلوب (قريبة جدًا بدون تراكب)
            int heartSpacing = -10;

// موقع البداية للقلب الأول (بعد مربع النقاط)
            int heartXStart = scorePlaceholderX + scorePlaceholderW + 15;
            int heartY = h - heartSizeH - 10;

            for (int i = 0; i < currentLives; i++) {
                int heartX = heartXStart + i * (heartSizeW + heartSpacing);

                lifeHeartTex.bind();
                gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0, 1); gl.glVertex2f(heartX, heartY);
                gl.glTexCoord2f(1, 1); gl.glVertex2f(heartX + heartSizeW, heartY);
                gl.glTexCoord2f(1, 0); gl.glVertex2f(heartX + heartSizeW, heartY + heartSizeH);
                gl.glTexCoord2f(0, 0); gl.glVertex2f(heartX, heartY + heartSizeH);
                gl.glEnd();
            }
        }
    }


    // دالة رسم قائمة الإيقاف المؤقت
    private void drawPauseMenu(GL gl, int w, int h) {
        // خلفية سوداء شفافة لتعتيم اللعبة
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(0, 0);
        gl.glVertex2f(w, 0);
        gl.glVertex2f(w, h);
        gl.glVertex2f(0, h);
        gl.glEnd();
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);

        // زر Resume
        if (btnResume != null) {
            btnResume.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(resumeX, resumeY);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(resumeX + resumeW, resumeY);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(resumeX + resumeW, resumeY + resumeH);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(resumeX, resumeY + resumeH);
            gl.glEnd();
        }
        // يمكن إضافة زر "الخروج للقائمة" هنا
    }

    // دالة رسم شاشة نهاية اللعبة
    private void drawGameOverScreen(GL gl, int w, int h) {
        // شاشة Game Over
        if (gameOverScreenTex != null) {
            gameOverScreenTex.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(gameOverX, gameOverY);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(gameOverX + gameOverW, gameOverY);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(gameOverX + gameOverW, gameOverY + gameOverH);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(gameOverX, gameOverY + gameOverH);
            gl.glEnd();
        }

        // زر Retry
        if (btnRetry != null) {
            btnRetry.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(retryX, retryY);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(retryX + retryW, retryY);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(retryX + retryW, retryY + retryH);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(retryX, retryY + retryH);
            gl.glEnd();
        }

        // عرض Final Score
        drawTextPlaceholder(gl, "Final Score: " + currentScore, retryX, retryY + 100);
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
            // الخلفية تبقى ثابتة في حالات اللعب/التوقف/نهاية اللعبة
        else if(gameState == STATE_GAME_PLAY || gameState == STATE_PAUSE || gameState == STATE_GAME_OVER) currentBackground = currentBgTex;

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
        else if(gameState == STATE_GAME_PLAY) {
            // رسم عناصر اللعب الأساسية هنا
            drawHUD(gl, w, h);
        }
        else if(gameState == STATE_PAUSE) {
            // رسم عناصر اللعب، ثم HUD، ثم قائمة الإيقاف فوق الكل
            // هنا لا نرسم عناصر اللعب الأساسية، فقط الخلفية والـ HUD
            drawHUD(gl, w, h);
            drawPauseMenu(gl, w, h);
        }
        else if(gameState == STATE_GAME_OVER) {
            drawGameOverScreen(gl, w, h);
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

            // Levels Selection
            if(mx >= level1X && mx <= level1X+levelW && my >= level1Y && my <= level1Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level1BgTex;
                currentLevel = 1; // تحديد المستوى الحالي
                // إعدادات المستوى الجديد
                currentScore = 0;
                currentLives = 3;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            } else if(mx >= level2X && mx <= level2X+levelW && my >= level2Y && my <= level2Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level2BgTex;
                currentLevel = 2; // تحديد المستوى الحالي
                currentScore = 0;
                currentLives = 3;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            } else if(mx >= level3X && mx <= level3X+levelW && my >= level3Y && my <= level3Y+levelH){
                if(clickSound != null) clickSound.play();
                currentBgTex = level3BgTex;
                currentLevel = 3; // تحديد المستوى الحالي
                currentScore = 0;
                currentLives = 3;
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            }
        }
        else if (gameState == STATE_GAME_PLAY) {
            // النقر على زر الإيقاف المؤقت (Pause)
            if(mx >= pauseX && mx <= pauseX+pauseW && my >= pauseY && my <= pauseY+pauseH){
                if(clickSound != null) clickSound.play();
                gameState = STATE_PAUSE;
                canvas.repaint();
            }
            // لتجربة شاشة نهاية اللعبة: افترض أن هناك شيئاً يجعل الأرواح صفر
            // if (currentLives > 0) currentLives--; else gameState = STATE_GAME_OVER;
        }
        else if (gameState == STATE_PAUSE) {
            // النقر على زر Resume (استئناف اللعب)
            if(mx >= resumeX && mx <= resumeX+resumeW && my >= resumeY && my <= resumeY+resumeH){
                if(clickSound != null) clickSound.play();
                gameState = STATE_GAME_PLAY;
                canvas.repaint();
            }
            // يمكن إضافة زر للعودة إلى شاشة اختيار المستويات هنا أيضًا
        }
        else if (gameState == STATE_GAME_OVER) {
            // النقر على زر Retry (إعادة المحاولة)
            if(mx >= retryX && mx <= retryX+retryW && my >= retryY && my <= retryY+retryH){
                if(clickSound != null) clickSound.play();
                // إعادة تهيئة المستوى الحالي
                currentScore = 0;
                currentLives = 3;
                gameState = STATE_GAME_PLAY; // العودة إلى اللعب
                canvas.repaint();
            }
        }
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}