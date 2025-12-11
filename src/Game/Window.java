package Game;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCanvas;
import java.awt.Frame;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import Player.GameTestCanvas;
import Player.SpaceShip;
import Player.Controls;

public class Window {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 800;

    public Window() {

        SwingUtilities.invokeLater(() -> {

            Frame frame = new Frame("Chicken pop");

            GLCapabilities caps = new GLCapabilities();
            GLCanvas menuCanvas = new GLCanvas(caps);

            GameManager gameManager = new GameManager();


            SpaceShip ship = new SpaceShip("src/assets/Space_ship.png");
            Controls controls = new Controls("src/assets/fire.png");


            GameTestCanvas gameCanvas = new GameTestCanvas(gameManager, ship, controls);
            gameCanvas.addKeyListener(controls);
            gameCanvas.setFocusable(true);


            MenuRenderer menuRenderer = new MenuRenderer(gameManager, menuCanvas, gameCanvas);


            Renderer renderer = new Renderer(menuCanvas, gameManager, menuRenderer, gameCanvas);
            menuCanvas.addGLEventListener(renderer);

            frame.setLayout(new CardLayout());
            frame.add(menuCanvas, "MENU");
            frame.add(gameCanvas, "GAME");

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

            menuCanvas.requestFocusInWindow();


            new Thread(() -> {
                int lastState = gameManager.getGameState();
                while (true) {
                    int state = gameManager.getGameState();
                    if (state != lastState) {
                        lastState = state;
                        SwingUtilities.invokeLater(() -> {
                            CardLayout cardLayout = (CardLayout) frame.getLayout();
                            if (state == GameManager.STATE_GAME_PLAY) {
                                cardLayout.show(frame, "GAME");
                                gameCanvas.requestFocus();
                            } else if (state == GameManager.STATE_MENU || state == GameManager.STATE_LEVEL_SELECT) {
                                cardLayout.show(frame, "MENU");
                                menuCanvas.requestFocus();
                            }
                        });
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) { /* ignore */ }
                }
            }).start();

        });
    }
}
