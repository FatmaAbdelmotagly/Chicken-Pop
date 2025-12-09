package Game;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.*;

public class Window {

    public Window() {

        Frame frame = new Frame("Chicken pop");
        GLCapabilities caps = new GLCapabilities();
        GLCanvas canvas = new GLCanvas(caps);

        Renderer renderer = new Renderer();
        canvas.addGLEventListener(renderer);

        frame.add(canvas);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);

            }
        });
    }
}
