package Game;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class Sound {

    // المتغير Clip لحفظ مسار الصوت وتشغيله
    private Clip clip;

    public Sound(String soundFileName) {
        try {
            // مسار الصوت داخل مجلد assets
            // لاحظي: ملفات الصوت لديك موجودة في مجلد "sounds" داخل "src"، لذا المسار هو sounds/soundFileName
            URL url = getClass().getClassLoader().getResource("sounds/" + soundFileName);

            if (url == null) {
                System.err.println("Sound file not found: sounds/" + soundFileName);
                return;
            }

            // 1. الحصول على مدخل تدفق الصوت (Audio Stream)
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            // 2. الحصول على كائن Clip
            clip = AudioSystem.getClip();

            // 3. فتح Clip بمدخل الصوت
            clip.open(audioIn);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // تشغيل الصوت من البداية
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // إعادة تعيين المؤشر للبداية
            clip.start();
        }
    }

    // تشغيل الصوت بشكل متكرر (للموسيقى الخلفية)
    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // التكرار اللانهائي
        }
    }

    // إيقاف الصوت
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}