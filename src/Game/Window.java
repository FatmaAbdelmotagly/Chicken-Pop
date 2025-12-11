package Game;

import Player.Controls;
import Player.GameTestCanvas;
import Player.SpaceShip;

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
            SpaceShip ship=new SpaceShip("Space_ship.png");
            Controls controls=new Controls("fire.png");

            GameManager gameManager = new GameManager();
            GameTestCanvas gameTestCanvas=new GameTestCanvas(ship,controls);

            MenuRenderer menuRenderer = new MenuRenderer(gameManager, canvas);


            Renderer renderer = new Renderer(canvas, gameManager, menuRenderer ,gameTestCanvas );
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