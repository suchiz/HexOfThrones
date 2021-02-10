package co.suchiz.hexofthrones;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {
    private final static SoundManager soundManagerInstance = new SoundManager();
    private SoundPool soundPool;
    private int menu_sound, tap_sound, start_sound;
    private Activity activity;
    private Boolean mute = true;

    private SoundManager(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();

    }

    public void playTap(){
        if(!mute)
            soundPool.play(tap_sound, 1, 1, 0,0, 1);
    }
    public void playMenu(){
        if(!mute)
            soundPool.play(menu_sound, 1, 1, 0,0, 1);
    }

    public void playStart(){
        if(!mute)
            soundPool.play(start_sound, 1, 1, 0,0, 1);

    }
    public void init(){
        menu_sound = soundPool.load(activity, R.raw.menu, 1);
        tap_sound = soundPool.load(activity, R.raw.tap, 1);
        start_sound = soundPool.load(activity, R.raw.start, 1);
    }

    public static SoundManager getSoundManagerInstance() {
        return soundManagerInstance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Boolean getMute() {
        return mute;
    }

    public void setMute(Boolean mute) {
        this.mute = mute;
    }

    public void release(){
        soundPool.release();
        soundPool = null;
    }

}
