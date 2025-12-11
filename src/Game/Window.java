package Game;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCanvas;
import javax.swing.SwingUtilities;

public class Window {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 800;


    public Window() {

        SwingUtilities.invokeLater(() -> {

            Frame frame = new Frame("Chicken pop");
            GLCapabilities caps = new GLCapabilities();
            GLCanvas canvas = new GLCanvas(caps);


            GameManager gameManager = new GameManager();


            MenuRenderer menuRenderer = new MenuRenderer(gameManager, canvas);


            Renderer renderer = new Renderer(canvas, gameManager, menuRenderer);
            canvas.addGLEventListener(renderer);


            frame.add(canvas);
            frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                    if (gameManager.getGameMusic() != null) {
                        gameManager.getGameMusic().stop();
                    }
                    frame.dispose();
                    System.exit(0);
                }
            });


            canvas.requestFocusInWindow();
        });
    }
}