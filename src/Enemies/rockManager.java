package Enemies;

import texture.TextureReader;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.io.IOException;
import texture.constants;

public class rockManager implements GLEventListener {

    String textureNames[] = {"Chicken_2.png","Chicken_1.png","Chicken_3.png","Chicken_4.png","rock.png","rock.png","rock.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    int x = constants.FRAME_WIDTH/2, y = constants.FRAME_HEIGHT/2;
    private int rock = 6;
    private int[]  rockX =  new int[rock];
    private int[]  rockY =  new int[rock];
    private float scale=1;
    String assetsFolderName = "Assets";
    public int[] getRockX() { return rockX; }
    public int[] getRockY() { return rockY; }

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
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
        for(int i = 0; i <rockX.length; i++){
            rockX [i] = 10+(int)(Math.random()*constants.FRAME_WIDTH) ;
            rockY [i] = constants.FRAME_HEIGHT+(int)(Math.random()*100) ;
        }

    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();

        gl.glLoadIdentity();
        for(int i = 0; i < rock; i++){
            DrawSprite(gl, rockX[i], rockY[i], 6, 1);
            UpdateRock(rockY[i], i );

        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }
    public void DrawSprite(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(constants.FRAME_WIDTH/2.0) - 0.9, y/(constants.FRAME_HEIGHT/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);

        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void UpdateRock(int y , int i){
        rockY[i]-=10;
        if(rockY[i]<0 ){
            rockY[i]=constants.FRAME_HEIGHT+(int)(Math.random()*100);
            rockX[i] = 10+(int)(Math.random()*constants.FRAME_WIDTH);
        }
    }

}



