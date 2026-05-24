package com.team.mazerunner.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private static final AudioManager INSTANCE = new AudioManager();
    private static final float MENU_MUSIC_VOLUME = 0.68f;
    private static final float GAME_MUSIC_VOLUME = 0.78f;
    private static final float BUTTON_VOLUME = 0.34f;
    private static final float SFX_VOLUME = 0.48f;
    private static final float IMPORTANT_SFX_VOLUME = 0.55f;

    private Music menuMusic;
    private Music gameMusic;
    private Sound clickSound;
    private Sound pickupSound;
    private Sound doorSound;
    private Sound damageSound;
    private Sound successSound;
    private Sound gameOverSound;
    private boolean loaded;
    private boolean musicMuted;
    private MusicMode activeMusicMode = MusicMode.NONE;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public void load() {
        if (loaded) {
            return;
        }

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menu_escape_chase_loop.wav"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(MENU_MUSIC_VOLUME);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/maze_escape_chase_loop.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(GAME_MUSIC_VOLUME);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.wav"));
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/pickup.wav"));
        doorSound = Gdx.audio.newSound(Gdx.files.internal("audio/door.wav"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("audio/damage.wav"));
        successSound = Gdx.audio.newSound(Gdx.files.internal("audio/success.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_over.wav"));
        loaded = true;
    }

    public void playMenuMusic() {
        load();
        activeMusicMode = MusicMode.MENU;
        if (musicMuted) {
            stopMusic();
            return;
        }

        if (gameMusic.isPlaying()) {
            gameMusic.stop();
        }
        if (!menuMusic.isPlaying()) {
            menuMusic.play();
        }
    }

    public void playGameMusic() {
        load();
        activeMusicMode = MusicMode.GAME;
        if (musicMuted) {
            stopMusic();
            return;
        }

        if (menuMusic.isPlaying()) {
            menuMusic.stop();
        }
        if (!gameMusic.isPlaying()) {
            gameMusic.play();
        }
    }

    public void stopMusic() {
        if (!loaded) {
            return;
        }

        menuMusic.stop();
        gameMusic.stop();
    }

    public void toggleMusic() {
        load();
        musicMuted = !musicMuted;

        if (musicMuted) {
            stopMusic();
            return;
        }

        if (activeMusicMode == MusicMode.MENU) {
            playMenuMusic();
        } else if (activeMusicMode == MusicMode.GAME) {
            playGameMusic();
        }
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void playClick() {
        play(clickSound, BUTTON_VOLUME);
    }

    public void playPickup() {
        play(pickupSound, SFX_VOLUME);
    }

    public void playDoor() {
        play(doorSound, SFX_VOLUME);
    }

    public void playDamage() {
        play(damageSound, IMPORTANT_SFX_VOLUME);
    }

    public void playStealthKill() {
        play(damageSound, IMPORTANT_SFX_VOLUME);
        play(successSound, 0.30f);
    }

    public void playSuccess() {
        play(successSound, IMPORTANT_SFX_VOLUME);
    }

    public void playGameOver() {
        play(gameOverSound, IMPORTANT_SFX_VOLUME);
    }

    private void play(Sound sound, float volume) {
        load();
        sound.play(volume);
    }

    public void dispose() {
        if (!loaded) {
            return;
        }

        menuMusic.dispose();
        gameMusic.dispose();
        clickSound.dispose();
        pickupSound.dispose();
        doorSound.dispose();
        damageSound.dispose();
        successSound.dispose();
        gameOverSound.dispose();
        loaded = false;
    }

    private enum MusicMode {
        NONE,
        MENU,
        GAME
    }
}
