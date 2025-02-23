package View;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip clip;
    private FloatControl volumeControl;

    public Sound(String soundFileName) {
initClip(soundFileName);
    }
    private void initClip(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error with playing sound: " + e.getMessage());
        }
    }

    public void playSound() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0);  // rewind to the beginning
            clip.start();  // Start playing
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float dB) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                // Đảm bảo giá trị không vượt quá giới hạn của FloatControl
                if (dB < gainControl.getMinimum()) {
                    dB = gainControl.getMinimum();
                }
                if (dB > gainControl.getMaximum()) {
                    dB = gainControl.getMaximum();
                }

                gainControl.setValue(dB); // Cập nhật âm lượng
            } catch (IllegalArgumentException e) {
                System.out.println("Volume control not supported: " + e.getMessage());
            }
        }
    }



    public Clip getClip() {
        return clip;
    }
}