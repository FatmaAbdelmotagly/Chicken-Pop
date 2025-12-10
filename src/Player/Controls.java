package Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Controls implements KeyListener {
    private BitSet keyBits = new BitSet(256);
    public List<Bullet> bullets = new ArrayList<>();
    private String bulletImagePath = "src/assets/Bullet.png";

    public Controls(String bulletImagePath) {
        this.bulletImagePath = bulletImagePath;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyBits.set(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyBits.clear(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public boolean isPressed(int keyCode) {
        return keyBits.get(keyCode);
    }

    public void handleKeyPress(SpaceShip ship) {
        if(isPressed(KeyEvent.VK_LEFT)) ship.moveLeft();
        if(isPressed(KeyEvent.VK_RIGHT)) ship.moveRight();
        if(isPressed(KeyEvent.VK_UP)) ship.moveUp();
        if(isPressed(KeyEvent.VK_DOWN)) ship.moveDown();
        if(isPressed(KeyEvent.VK_SPACE)) {
            fireBullet(ship);
        }

    }
    private void fireBullet(SpaceShip ship) {
        int bulletWidth = 50;
        int bulletHeight = 80;

        float startX = ship.getX() + ship.getWidth()/2.0f - bulletWidth/2.0f;


        float startY = ship.getY() + ship.getHeight() - bulletHeight;

        bullets.add(new Bullet(bulletImagePath, startX, startY, bulletWidth, bulletHeight));
    }

}
