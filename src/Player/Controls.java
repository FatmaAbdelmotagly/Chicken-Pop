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
    public int level = 1;

    private long lastFireTime = 0;
    private long fireCooldown = 250;
    private int score = 0;

    public Controls(String bulletImagePath) {
        this.bulletImagePath = bulletImagePath;
    }


    public void setLevel(int level) {
        if (level < 1) level = 1;
        if (level > 3) level = 3;
        this.level = level;
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
        long now = System.currentTimeMillis();
        if (now - lastFireTime < fireCooldown) return;
        lastFireTime = now;

        int bulletWidth = 50;
        int bulletHeight = 80;

        float startY = ship.getY() + ship.getHeight() - bulletHeight;

        float center = ship.getX() + ship.getWidth() / 2f - bulletWidth / 2f;
        float left   = ship.getX() + ship.getWidth() * 0.25f - bulletWidth / 2f;
        float right  = ship.getX() + ship.getWidth() * 0.75f - bulletWidth / 2f;

        if (level == 1) {
            bullets.add(new Bullet(bulletImagePath, center, startY, bulletWidth, bulletHeight));
        }
        else if (level == 2) {
            bullets.add(new Bullet(bulletImagePath, left, startY, bulletWidth, bulletHeight));
            bullets.add(new Bullet(bulletImagePath, right, startY, bulletWidth, bulletHeight));
        }
        else if (level == 3) {
            bullets.add(new Bullet(bulletImagePath, left, startY, bulletWidth, bulletHeight));
            bullets.add(new Bullet(bulletImagePath, center, startY, bulletWidth, bulletHeight));
            bullets.add(new Bullet(bulletImagePath, right, startY, bulletWidth, bulletHeight));
        }
    }
}
