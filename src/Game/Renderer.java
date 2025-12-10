package Game;

import javax.media.opengl.*;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.media.opengl.GLCanvas;
import java.io.IOException;
import java.io.InputStream;

public class Renderer implements GLEventListener, MouseListener {

    private final GameManager gameManager;
    private final MenuRenderer menuRenderer;
    private final GLCanvas canvas;

    // HUD Textures
    private Texture lifeHeartTex;

    // ------------------------------------------------------------------
    // 🎨 الباني (Constructor)
    // ------------------------------------------------------------------

    public Renderer(GLCanvas canvas, GameManager gameManager, MenuRenderer menuRenderer){
        this.canvas = canvas;
        this.gameManager = gameManager;
        this.menuRenderer = menuRenderer;
        canvas.addMouseListener(this);
    }

    /**
     * دالة مُحسَّنة لتحميل Texture باستخدام InputStream.
     */
    private Texture loadTexture(String path) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);

        if (stream == null) {
            stream = getClass().getResourceAsStream("/" + path);
        }

        if (stream != null) {
            try {
                return TextureIO.newTexture(stream, false, TextureIO.PNG);
            } finally {
                stream.close();
            }
        }

        System.err.println("TEXTURE NOT FOUND: " + path);
        return null;
    }

    // ------------------------------------------------------------------
    // ⚙️ تهيئة OpenGL (INIT)
    // ------------------------------------------------------------------

    public void init(GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glClearColor(0f,0f,0f,1f);

        int canvasW = drawable.getWidth();
        int canvasH = drawable.getHeight();

        // تحميل Texture الـ HUD (القلوب)
        try {
            lifeHeartTex = loadTexture("assets/heart.png");
        } catch(IOException e){
            System.err.println("Failed to load heart texture: " + e.getMessage());
        }

        // تهيئة MenuRenderer (لتحميل Textures القوائم وحساب المواقع)
        menuRenderer.init(gl, canvasW, canvasH);
    }

    // ------------------------------------------------------------------
    // ✍️ دوال الرسم المساعدة (HUD و Text Placeholder)
    // ------------------------------------------------------------------

    /**
     * دالة مساعدة لعرض النص (تستخدم مربع مؤقت بدلاً من الخطوط).
     */
    private void drawTextPlaceholder(GL gl, String text, int x, int y, int w, int h) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 0.0f); // لون أصفر للنقاط
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x, y + h);
        gl.glEnd();
        gl.glColor4f(1.0f,1.0f,1.0f, 1.0f); // إعادة اللون
        gl.glEnable(GL.GL_TEXTURE_2D);
        // ملاحظة: هنا يمكن دمج مكتبة رسم الخطوط (مثل GLText) لاحقاً
    }

    /**
     * دالة رسم الـ HUD (النقاط والأرواح وزر الإيقاف المؤقت).
     */
    private void drawHUD(GL gl, int w, int h) {

        // 1. زر الإيقاف المؤقت (Pause Button)
        if (gameManager.getGameState() == GameManager.STATE_GAME_PLAY || gameManager.getGameState() == GameManager.STATE_PAUSE) {
            if (menuRenderer.btnPause != null) {
                menuRenderer.drawButton(gl, menuRenderer.btnPause, menuRenderer.pauseX, menuRenderer.pauseY, menuRenderer.pauseW, menuRenderer.pauseH);
            }
        }

        // 2. Score (النقاط)
        int scorePlaceholderW = 150;
        int scorePlaceholderH = 25;
        int scoreTextX = 20;
        int scoreTextY = h - 50;

        drawTextPlaceholder(gl, "SCORE: " + gameManager.getCurrentScore(),
                scoreTextX, scoreTextY, scorePlaceholderW, scorePlaceholderH);


        // 3. الأرواح (Lives)
        if (lifeHeartTex != null) {

            int heartSizeW = 120;
            int heartSizeH = 55;
            int heartSpacing = -10;

            int heartXStart = scoreTextX + scorePlaceholderW + 15;
            int heartY = h - heartSizeH - 10;
            int currentLives = gameManager.getCurrentLives();

            for (int i = 0; i < currentLives; i++) {
                int heartX = heartXStart + i * (heartSizeW + heartSpacing);

                // استخدام دالة drawButton من MenuRenderer لرسم القلب
                menuRenderer.drawButton(gl, lifeHeartTex, heartX, heartY, heartSizeW, heartSizeH);
            }
        }
    }


    // ------------------------------------------------------------------
    // 🖼️ دالة العرض الرئيسية (DISPLAY)
    // ------------------------------------------------------------------

    public void display(GLAutoDrawable drawable){
        GL gl = drawable.getGL();
        int w = drawable.getWidth();
        int h = drawable.getHeight();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // إعداد الإسقاط (Projection) ليكون 2D
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, w, 0, h, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // رسم الخلفية
        menuRenderer.drawCurrentBackground(gl, w, h);

        int state = gameManager.getGameState();

        if(state == GameManager.STATE_MENU) {
            menuRenderer.drawMenuScreen(gl);
        } else if(state == GameManager.STATE_LEVEL_SELECT) {
            menuRenderer.drawLevelSelectionScreen(gl);
        } else if(state == GameManager.STATE_GAME_PLAY) {
            // هنا يتم رسم عناصر اللعب الفعلية (الدجاج، البيض، إلخ)
            drawHUD(gl, w, h);
        } else if(state == GameManager.STATE_PAUSE) {
            drawHUD(gl, w, h);
            menuRenderer.drawPauseMenu(gl, w, h);
        } else if(state == GameManager.STATE_GAME_OVER) {
            menuRenderer.drawGameOverScreen(gl, w, h);
        }
    }

    // ------------------------------------------------------------------
    // 📐 دوال أحداث OpenGL الأخرى
    // ------------------------------------------------------------------

    public void reshape(GLAutoDrawable d, int x, int y, int w, int h){
        d.getGL().glViewport(0,0,w,h);
        // تحديث مواقع القوائم عند تغيير حجم الشاشة
        menuRenderer.calculatePositions(w, h);
    }

    public void dispose(GLAutoDrawable drawable){}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged){}

    // ------------------------------------------------------------------
    // 🖱️ أحداث الماوس (Mouse Listener)
    // ------------------------------------------------------------------

    public void mouseClicked(MouseEvent e){
        // تحويل إحداثيات الماوس من نظام AWT (الأصل من الأعلى) إلى نظام OpenGL (الأصل من الأسفل)
        int my = canvas.getHeight() - e.getY();
        int mx = e.getX();

        // تمرير حدث النقر إلى الـ MenuRenderer لمعالجة الأزرار
        menuRenderer.handleMouseClick(mx, my);
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}