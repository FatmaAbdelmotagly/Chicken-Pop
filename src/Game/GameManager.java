package Game;

import javax.media.opengl.GLCanvas;
import com.sun.opengl.util.texture.Texture;

public class GameManager {


    public static final int STATE_MENU = 0;
    public static final int STATE_LEVEL_SELECT = 1;
    public static final int STATE_GAME_PLAY = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_GAME_OVER = 4;

    private int gameState = STATE_MENU;
    private int currentLevel = 0;
    private int currentScore = 0;
    private int currentLives = 3;
    private Texture currentBgTex = null;


    private Sound clickSound;
    private Sound gameMusic;

    public GameManager() {

        clickSound = new Sound("click.wav");
        gameMusic = new Sound("game sound.wav");
        if(gameMusic != null) gameMusic.loop();
    }



    public int getGameState() { return gameState; }
    public void setGameState(int gameState) { this.gameState = gameState; }

    public int getCurrentScore() { return currentScore; }
    public void setCurrentScore(int currentScore) { this.currentScore = currentScore; }

    public int getCurrentLives() { return currentLives; }
    public void setCurrentLives(int currentLives) { this.currentLives = currentLives; }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }

    public Texture getCurrentBgTex() { return currentBgTex; }
    public void setCurrentBgTex(Texture currentBgTex) { this.currentBgTex = currentBgTex; }

    public Sound getClickSound() { return clickSound; }
    public Sound getGameMusic() { return gameMusic; }


    public void resetLevel(int level, Texture bg) {
        currentLevel = level;
        currentBgTex = bg;
        currentScore = 0;
        currentLives = 3;
        gameState = STATE_GAME_PLAY;
    }
}