package Enemies;
import com.sun.opengl.util.FPSAnimator;
import texture.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChickenManager implements GLEventListener {


    List<Chickens>chickensGroup;
    int animationIndex = 0;
    static FPSAnimator animator =null;
    String assetsFolderName = "Assets";
    String textureNames[] = {"Chicken_2.png","Chicken_1.png","Chicken_3.png","Chicken_4.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];



    public void level(int level){
        if(level==1){
            chickensGroup=Chicken_groub(3,3,-7,0,5,2);
        } else if (level==2) {
            chickensGroup= Chicken_groub(4,4,-8,0,4,2);
        } else {
            chickensGroup=Chicken_groub(5,5,-9,-2,4,2);
        }
    }
    public List<Chickens> Chicken_groub (int rows, int cols, int startX, int startY, int gapX, int gapY) {
        List<Chickens> chickens = new ArrayList<>();
        for(int i=0; i<rows; i++){
            int y = startY + i * gapY;
            for(int j=0; j<cols; j++){
                int x = startX + j * gapX;
                chickens.add(new Chickens(x, y));
            }
        }
        return chickens;
    }

    public void draw(GL gl ,int x, int y, int animationIndex){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[animationIndex]);


        gl.glPushMatrix();
        gl.glTranslated(0, 0.2, 0);
        gl.glScaled(0.1, 0.1, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(x, y, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(x+2, y, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(x+2, y+2, -1.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(x, y+2, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);


    }

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        animationIndex = animationIndex % 4;


        for (Chickens chicken : chickensGroup) {
            draw(gl, chicken.x, chicken.y, animationIndex);
        }

        animationIndex++;


    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {}
}


