package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Util.UtilityTools;

/**
 * Lớp quản lý âm thanh có tác dụng chạy âm thanh cho toàn bộ hệ thống
 * <p>
 * Lớp này cho phép chạy 1 bản nhạc nền và nhiều hiệu ứng âm thanh cùng lúc.
 */
public class SoundManager {

    private static final SoundManager instance = new SoundManager();

    public static SoundManager getInstance() {
        return instance;
    }

    private Map<SoundType, File> mp = new HashMap<>();

    private Clip music;
    private ArrayList<Clip> soundEffects = new ArrayList<>();

    private boolean bgEnabled = true, seEnabled = true;

    private SoundManager() {
        init();
    }

    public void playMusic(SoundType type) {
        if (music != null) {
            music.stop();
            music.close();
        }
        music = this.getClip(mp.get(type));
        if (bgEnabled == false)
            return;
        music.start();
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSE(SoundType type) {
        // Xoá các hiệu ứng âm thanh đã chạy xong
        for (int i = 0; i < soundEffects.size(); ++i) {
            Clip tmp = soundEffects.get(i);
            if (tmp.isRunning() == false) {
                tmp.close();
                soundEffects.remove(i);
                --i;
            }
        }

        if (seEnabled == false)
            return;
        Clip se = this.getClip(mp.get(type));
        se.start();
        soundEffects.add(se);
    }

    public void setBackgroundState(boolean state) {
        bgEnabled = state;
        if (music == null)
            return;
        if (bgEnabled) {
            music.start();
        } else {
            music.stop();
        }
    }

    public boolean getMusicState() {
        return bgEnabled;
    }

    public void setSoundEffectState(boolean state) {
        seEnabled = state;
    }

    private void init() {
        try {
            File[] files = UtilityTools.getFiles("/Resources/Audios", ".wav");
            for (File file : files) {
                if (file.getName().startsWith("background_1"))
                    mp.put(SoundType.BACKGROUND_1, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Clip getClip(File file) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
