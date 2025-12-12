package Game;

import javax.media.opengl.GL;
import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GLCanvas;
import com.sun.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;


public class MenuRenderer {

    private final GameManager gameManager;
    private final GLCanvas canvas;
    private final Player.GameTestCanvas gameCanvas;

    public Texture background, levelBackground;
    public Texture btnStart, btnExit;
    public Texture level1Tex, level2Tex, level3Tex;
    public Texture btnBack;
    public Texture level1BgTex, level2BgTex, level3BgTex;
    public Texture btnPause, btnResume, btnHome;
    public Texture gameOverScreenTex, btnRetry;

    public int startX, startY, startW, startH;
    public int exitX, exitY, exitW, exitH;
    public int level1X, level1Y, level2X, level2Y, level3X, level3Y, levelW, levelH;
    public int backX, backY, backW, backH;
    public int pauseX, pauseY, pauseW, pauseH;
    public int resumeX, resumeY, resumeW, resumeH;
    public int retryX, retryY, retryW, retryH;
    public int gameOverX, gameOverY, gameOverW, gameOverH;
    public int homeX, homeY, homeW, homeH;


    public MenuRenderer(GameManager gameManager, GLCanvas canvas, Player.GameTestCanvas gameCanvas) {
        this.gameManager = gameManager;
        this.canvas = canvas;
        this.gameCanvas = gameCanvas;
    }

    private Texture loadTexture(String path) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) stream = getClass().getResourceAsStream("/" + path);
        if (stream != null) {
            try {
                return TextureIO.newTexture(stream, false, TextureIO.PNG);
            } finally {
                stream.close();
            }
        }
        System.err.println("TEXTURE NOT FOUND or failed to load: " + path);
        return null;
    }

    public void init(GL gl, int canvasW, int canvasH) {
        try {
            background = loadTexture("assets/back_1.png");
            levelBackground = loadTexture("assets/back2.png");
            btnStart = loadTexture("assets/sst.png");
            btnExit = loadTexture("assets/eex.png");
            level1Tex = loadTexture("assets/lvl1.png");
            level2Tex = loadTexture("assets/lvl2.png");
            level3Tex = loadTexture("assets/lvl3.png");
            btnBack = loadTexture("assets/bb.png");
            level1BgTex = loadTexture("assets/back2.png");
            level2BgTex = loadTexture("assets/back2.png");
            level3BgTex = loadTexture("assets/back2.png");
            btnPause = loadTexture("assets/pause.png");
            btnResume = loadTexture("assets/resume.png");
            gameOverScreenTex = loadTexture("assets/gameover.png");
            btnRetry = loadTexture("assets/retry.png");
            btnHome = loadTexture("assets/bb2.png");


        } catch(IOException e){
            System.err.println("Failed to load resources: " + e.getMessage());
        }

        calculatePositions(canvasW, canvasH);
    }

    public void calculatePositions(int canvasW, int canvasH) {
        startW = exitW = 300; startH = exitH = 150;
        int spacing = 30;
        startX = (canvasW - startW)/2;
        exitX  = startX;
        startY = (canvasH/2) - (startH/2) - 50;
        exitY  = startY - exitH - spacing;

        levelW = 350; levelH = 130;
        int padding = 30;
        int levelX = (canvasW - levelW)/2;
        int totalHeight = levelH*3 + padding*2;
        int startYPos = (canvasH/2) - (totalHeight/2);

        level3X = levelX; level3Y = startYPos;
        level2X = levelX; level2Y = startYPos + levelH + padding;
        level1X = levelX; level1Y = startYPos + (levelH*2) + (padding*2);

        backW = 150; backH = 70;
        backX = 20; backY = canvasH - backH - 20;

        pauseW = 150; pauseH = 70;
        pauseX = canvasW - pauseW - 20;
        pauseY = canvasH - pauseH - 20;

        resumeW = 250; resumeH = 100;
        resumeX = (canvasW - resumeW) / 2;
        resumeY = (canvasH / 2) + 60;

        homeW = 200; homeH = 80;
        homeX = (canvasW - homeW) / 2;
        homeY = resumeY - homeH - 40;

        gameOverW = 600; gameOverH = 400;
        gameOverX = (canvasW - gameOverW) / 2;
        gameOverY = (canvasH - gameOverH) / 2;

        retryW = 200; retryH = 80;
        retryX = (canvasW - retryW) / 2;
        retryY = gameOverY + 50;
    }

    public void drawCurrentBackground(GL gl, int w, int h) {
        Texture currentBackground = null;
        int state = gameManager.getGameState();

        if (state == GameManager.STATE_MENU) currentBackground = background;
        else if (state == GameManager.STATE_LEVEL_SELECT) currentBackground = levelBackground;
        else if (state >= GameManager.STATE_GAME_PLAY) {
            currentBackground = gameManager.getCurrentBgTex();
        }

        if (currentBackground != null) {
            currentBackground.bind();
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0, 1); gl.glVertex2f(0, 0);
            gl.glTexCoord2f(1, 1); gl.glVertex2f(w, 0);
            gl.glTexCoord2f(1, 0); gl.glVertex2f(w, h);
            gl.glTexCoord2f(0, 0); gl.glVertex2f(0, h);
            gl.glEnd();
        }
    }

    public void drawMenuScreen(GL gl) {
        if (btnStart != null) drawButton(gl, btnStart, startX, startY, startW, startH);
        if (btnExit != null) drawButton(gl, btnExit, exitX, exitY, exitW, exitH);
    }

    public void drawLevelSelectionScreen(GL gl) {
        if (level1Tex != null) drawButton(gl, level1Tex, level1X, level1Y, levelW, levelH);
        if (level2Tex != null) drawButton(gl, level2Tex, level2X, level2Y, levelW, levelH);
        if (level3Tex != null) drawButton(gl, level3Tex, level3X, level3Y, levelW, levelH);
//        if (btnBack != null) drawButton(gl, btnBack, backX, backY, backW, backH);
    }

    public void drawPauseMenu(GL gl, int w, int h) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(0, 0); gl.glVertex2f(w, 0); gl.glVertex2f(w, h); gl.glVertex2f(0, h);
        gl.glEnd();
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        if (btnResume != null) drawButton(gl, btnResume, resumeX, resumeY, resumeW, resumeH);
        if (btnHome != null) drawButton(gl, btnHome, homeX, homeY, homeW, homeH);
    }

    public void drawGameOverScreen(GL gl, int w, int h) {
        if (gameOverScreenTex != null) drawButton(gl, gameOverScreenTex, gameOverX, gameOverY, gameOverW, gameOverH);
        if (btnRetry != null) drawButton(gl, btnRetry, retryX, retryY, retryW, retryH);

        int placeholderW = 250;
        int placeholderH = 30;
        int placeholderY = retryY + 100;
        int placeholderX = (w - placeholderW) / 2;

        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(placeholderX, placeholderY);
        gl.glVertex2f(placeholderX + placeholderW, placeholderY);
        gl.glVertex2f(placeholderX + placeholderW, placeholderY + placeholderH);
        gl.glVertex2f(placeholderX, placeholderY + placeholderH);
        gl.glEnd();
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
    }

    public void drawButton(GL gl, Texture tex, int x, int y, int w, int h) {
        if (tex == null) return;
        tex.bind();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(x, y);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(x + w, y);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(x + w, y + h);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(x, y + h);
        gl.glEnd();
    }

    public void handleMouseClick(int mx, int my) {
        int state = gameManager.getGameState();

        if (state == GameManager.STATE_MENU) {
            if (isClicked(mx, my, startX, startY, startW, startH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.setGameState(GameManager.STATE_LEVEL_SELECT);
            } else if (isClicked(mx, my, exitX, exitY, exitW, exitH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                if (gameManager.getGameMusic() != null) gameManager.getGameMusic().stop();
                System.exit(0);
            }
        } else if (state == GameManager.STATE_LEVEL_SELECT) {
            if (isClicked(mx, my, backX, backY, backW, backH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.setGameState(GameManager.STATE_MENU);
            } else if (isClicked(mx, my, level1X, level1Y, levelW, levelH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.resetLevel(1, level1BgTex);
                // Important: update gameCanvas so ChickenManager/ship know the level before first frame
                if (gameCanvas != null) gameCanvas.setLevel(1);
            }
            else if (isClicked(mx,my, level2X, level2Y, levelW, levelH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.resetLevel(2, level2BgTex);
                if (gameCanvas != null) gameCanvas.setLevel(2);
            }
            else if (isClicked(mx, my, level3X, level3Y, levelW, levelH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.resetLevel(3, level3BgTex);
                if (gameCanvas != null) gameCanvas.setLevel(3);
            }
        } else if (state == GameManager.STATE_GAME_PLAY) {
            if (isClicked(mx, my, pauseX, pauseY, pauseW, pauseH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.setGameState(GameManager.STATE_PAUSE);
            }
        } else if (state == GameManager.STATE_PAUSE) {
            if (isClicked(mx, my, resumeX, resumeY, resumeW, resumeH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.setGameState(GameManager.STATE_GAME_PLAY);
            } else if (isClicked(mx, my, homeX, homeY, homeW, homeH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.setGameState(GameManager.STATE_LEVEL_SELECT);
            }
        } else if (state == GameManager.STATE_GAME_OVER) {
            if (isClicked(mx, my, retryX, retryY, retryW, retryH)) {
                if (gameManager.getClickSound() != null) gameManager.getClickSound().play();
                gameManager.resetLevel(gameManager.getCurrentLevel(), gameManager.getCurrentBgTex());
                if (gameCanvas != null) gameCanvas.setLevel(gameManager.getCurrentLevel());
            }
        }
        canvas.repaint();
    }

    private boolean isClicked(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
