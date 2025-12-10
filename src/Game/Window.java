package Game;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCanvas;
import javax.swing.SwingUtilities; // لا يزال مهماً لضمان تشغيل واجهة المستخدم بشكل صحيح

public class Window {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 800;

    // هذا هو الباني (Constructor) الذي سيتم استدعاؤه من الكلاس الخارجي
    public Window() {
        // نضمن أن يتم تهيئة واجهة المستخدم الرسومية في AWT Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // 1. إنشاء الإطار والـ Canvas
            Frame frame = new Frame("Chicken pop");
            GLCapabilities caps = new GLCapabilities();
            GLCanvas canvas = new GLCanvas(caps);

            // 2. إنشاء مدير اللعبة (GameManager)
            GameManager gameManager = new GameManager();

            // 3. إنشاء رسام القوائم (MenuRenderer)
            MenuRenderer menuRenderer = new MenuRenderer(gameManager, canvas);

            // 4. إنشاء الرسام الرئيسي (Renderer)
            Renderer renderer = new Renderer(canvas, gameManager, menuRenderer);
            canvas.addGLEventListener(renderer);

            // 5. إعداد الإطار
            frame.add(canvas);
            frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            frame.setLocationRelativeTo(null); // توسيط النافذة
            frame.setVisible(true);

            // 6. التعامل مع إغلاق النافذة
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // إيقاف الموسيقى قبل الخروج
                    if (gameManager.getGameMusic() != null) {
                        gameManager.getGameMusic().stop();
                    }
                    frame.dispose();
                    System.exit(0);
                }
            });

            // التركيز على Canvas لاستقبال مدخلات لوحة المفاتيح والماوس
            canvas.requestFocusInWindow();
        });
    }
}