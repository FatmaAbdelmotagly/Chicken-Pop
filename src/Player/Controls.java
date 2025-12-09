package Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;

public class Controls implements KeyListener {
    private BitSet keyBits = new BitSet(256);

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
    }
}
