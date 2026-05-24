package com.team.mazerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.team.mazerunner.audio.AudioManager;
import com.team.mazerunner.screens.MainMenuScreen;

public class Main extends Game {

    public SpriteBatch batch;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    @Override
    public void create() {

        batch = new SpriteBatch();
        AudioManager.getInstance().load();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {

        batch.dispose();
        AudioManager.getInstance().dispose();
        super.dispose();
    }
}
