package com.team.mazerunner.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.team.mazerunner.Main;

public class Lwjgl3Launcher {

    public static void main(String[] args) {

        if (StartupHelper.startNewJvmIfRequired()) return;

        createApplication();
    }

    private static Lwjgl3Application createApplication() {

        return new Lwjgl3Application(
            new Main(),
            getDefaultConfiguration()
        );
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {

        Lwjgl3ApplicationConfiguration configuration =
            new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Maze Runner");

        configuration.setWindowedMode(
            Main.SCREEN_WIDTH,
            Main.SCREEN_HEIGHT
        );

        configuration.useVsync(true);

        configuration.setForegroundFPS(60);

        return configuration;
    }
}
