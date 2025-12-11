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
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
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
     //   gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        for(int i = 0; i < rock; i++){
            DrawSprite(gl, rockX[i], rockY[i], 6, 1);
            UpdateRock(rockY[i], i );
           // checkBlueCollision(i);
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

    public boolean isColliding(int x1, int y1, int w1, int h1,
                               int x2, int y2, int w2, int h2,float scale) {
        int realW1 = (int)(w1 * scale);
        int realH1 = (int)(h1 * scale);
        return x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2;
    }
//    public void checkBlueCollision(int i) {
//        int w = 60, h = 60;
//
//        if (isColliding(x, y, w, h, blueX[i], blueY[i], w, h ,scale)) {
//            // Example: remove monster
//            blueY[i] = -1000;
//            blueX[i] = 10+(int)(Math.random()*maxWidth);
//            // Example effect: make soldier bigger
//            scale+= 0.2f;
//
//
////            System.out.println("Touched BLUE monster " + i);
//        }
    }
//    public void checkRedCollision(int i) {
//        int w = 60, h = 60;
//
//        if (isColliding(x, y, w, h, redX[i], redY[i], w, h,scale)) {
//            redY[i] = -1000;
//            redX[i] = 10+(int)(Math.random()*maxWidth);// remove monster
//            scale -= 0.2f;              // أنقص الحجم بمقدار 0.2
//            if (scale < 0.3f) scale = 0.3f;
//
//        }
//    }


