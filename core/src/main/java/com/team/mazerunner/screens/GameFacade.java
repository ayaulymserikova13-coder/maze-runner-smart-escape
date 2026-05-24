package com.team.mazerunner.screens;

import com.team.mazerunner.Main;

public class GameFacade {

    private final Main game;

    public GameFacade(Main game) {
        this.game = game;
    }

    public void startGame() {
        startGame(1);
    }

    public void startGame(int levelNumber) {
        game.setScreen(new GameScreen(game, levelNumber));
    }

    public void restartLevel(int levelNumber) {
        startGame(levelNumber);
    }

    public void goToMainMenu() {
        game.setScreen(new MainMenuScreen(game));
    }

    public void showGameOver(int levelNumber) {
        game.setScreen(new GameOverScreen(game, levelNumber));
    }

    public void showWin() {
        game.setScreen(new WinScreen(game));
    }
}
