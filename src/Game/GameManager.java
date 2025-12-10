package Game;

import javax.media.opengl.GLCanvas;
import com.sun.opengl.util.texture.Texture;

public class GameManager {

    // Game States (ثوابت عامة)
    public static final int STATE_MENU = 0;
    public static final int STATE_LEVEL_SELECT = 1;
    public static final int STATE_GAME_PLAY = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_GAME_OVER = 4;

    private int gameState = STATE_MENU;
    private int currentLevel = 0; // 1, 2, or 3
    private int currentScore = 0;
    private int currentLives = 3;
    private Texture currentBgTex = null; // خلفية المستوى الحالي

    // Sounds
    private Sound clickSound;
    private Sound gameMusic;

    public GameManager() {
        // يمكنك تهيئة الأصوات هنا إذا كان ملف Sound متاحًا
        clickSound = new Sound("click.wav");
        gameMusic = new Sound("game sound.wav");
        if(gameMusic != null) gameMusic.loop();
    }

    // --- Getters and Setters ---

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

    // دالة لإعادة تهيئة المستوى عند بدء مستوى جديد أو المحاولة مرة أخرى
    public void resetLevel(int level, Texture bg) {
        currentLevel = level;
        currentBgTex = bg;
        currentScore = 0;
        currentLives = 3;
        gameState = STATE_GAME_PLAY;
    }
}