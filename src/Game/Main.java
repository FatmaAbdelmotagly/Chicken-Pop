package Game;

import Player.Controls;
import Player.GameTestCanvas;
import Player.SpaceShip;

import javax.media.opengl.GLCanvas;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 1) إنشاء Window
        JFrame frame = new JFrame("Chicken Invaders");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2) إنشاء Canvas
        GLCanvas canvas = new GLCanvas();




        // 3) إنشاء GameManager
        GameManager gameManager = new GameManager();

        // 4) إنشاء MenuRenderer
        MenuRenderer menuRenderer = new MenuRenderer(gameManager,canvas);

        // 5) إنشاء عناصر Gameplay
        SpaceShip ship = new SpaceShip("Space_ship.png");
        Controls controls = new Controls("fire.png");
        GameTestCanvas gamePlayRenderer = new GameTestCanvas(ship, controls);

        // 6) إنشاء Renderer (اللي يجمع كله)
        Renderer renderer = new Renderer(canvas, gameManager, menuRenderer, gamePlayRenderer);

        // 7) إضافة Renderer
        canvas.addGLEventListener(renderer);

        // 8) إضافة KeyListener لو عندك Controls
        canvas.addKeyListener(controls);
        canvas.setFocusable(true);

        // 9) وضع Canvas في Window
        frame.add(canvas);
        frame.setVisible(true);
    }
}
