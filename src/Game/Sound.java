package Game;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class Sound {

    private Clip clip;

    public Sound(String soundFileName) {
        try {

            URL url = getClass().getClassLoader().getResource("sounds/" + soundFileName);

            if (url == null) {
                System.err.println("Sound file not found: sounds/" + soundFileName);
                return;
            }


            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);


            clip = AudioSystem.getClip();


            clip.open(audioIn);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }


    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // إيقاف الصوت
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}