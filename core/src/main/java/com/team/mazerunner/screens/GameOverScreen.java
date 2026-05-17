package com.team.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team.mazerunner.Main;
import com.team.mazerunner.audio.AudioManager;

public class GameOverScreen implements Screen {

    private final Main game;
    private final int levelNumber;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final GameFacade gameFacade;
    private MenuButton retryButton;
    private MenuButton menuButton;

    public GameOverScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.gameFacade = new GameFacade(game);
        createButtons();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.030f, 0.026f, 0.030f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderDarkPanelBackground(new Color(0.16f, 0.035f, 0.035f, 1f));
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 210, 178, 420, 340, new Color(0.92f, 0.24f, 0.18f, 1f));
        drawSkull(Main.SCREEN_WIDTH / 2f, 446);
        retryButton.drawShape(shapeRenderer, retryButton.contains(mouseX, mouseY));
        menuButton.drawShape(shapeRenderer, menuButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(new Color(1.00f, 0.34f, 0.26f, 1f));
        font.getData().setScale(1.8f);
        font.draw(game.batch, "GAME OVER", Main.SCREEN_WIDTH / 2f - 95, 405);
        font.getData().setScale(1f);
        font.setColor(new Color(1.00f, 0.46f, 0.38f, 1f));
        font.draw(game.batch, "You were caught by the enemy.", Main.SCREEN_WIDTH / 2f - 101, 366);
        retryButton.drawText(game.batch, font);
        menuButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height, Color accentColor) {
        shapeRenderer.setColor(new Color(0.006f, 0.003f, 0.003f, 1f));
        shapeRenderer.rect(x + 10, y - 10, width, height);
        shapeRenderer.setColor(new Color(accentColor.r, accentColor.g, accentColor.b, 1f));
        shapeRenderer.rect(x, y, width, 5);
        shapeRenderer.rect(x, y + height - 5, width, 5);
        shapeRenderer.rect(x, y, 5, height);
        shapeRenderer.rect(x + width - 5, y, 5, height);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 18, y + height - 24, width - 36, 2);
        shapeRenderer.rect(x + 18, y + 24, width - 36, 2);
        shapeRenderer.setColor(new Color(0.025f, 0.010f, 0.010f, 1f));
        shapeRenderer.rect(x + 6, y + 6, width - 12, height - 12);
        drawPanelGrid(x + 24, y + 36, width - 48, height - 72, new Color(0.13f, 0.035f, 0.035f, 1f));
        shapeRenderer.setColor(new Color(1.00f, 0.42f, 0.34f, 1f));
        shapeRenderer.rect(x + 10, y + 10, 8, 8);
        shapeRenderer.rect(x + width - 18, y + 10, 8, 8);
        shapeRenderer.rect(x + 10, y + height - 18, 8, 8);
        shapeRenderer.rect(x + width - 18, y + height - 18, 8, 8);
    }

    private void renderDarkPanelBackground(Color accentColor) {
        shapeRenderer.setColor(new Color(0.014f, 0.012f, 0.014f, 1f));
        shapeRenderer.rect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        drawPanelGrid(70, 55, Main.SCREEN_WIDTH - 140, Main.SCREEN_HEIGHT - 110,
                new Color(accentColor.r * 0.40f, accentColor.g * 0.40f, accentColor.b * 0.40f, 1f));
    }

    private void drawPanelGrid(float x, float y, float width, float height, Color color) {
        shapeRenderer.setColor(color);
        for (float gx = x; gx < x + width; gx += 30) {
            shapeRenderer.rect(gx, y, 1, height);
        }
        for (float gy = y; gy < y + height; gy += 30) {
            shapeRenderer.rect(x, gy, width, 1);
        }
    }

    private void drawSkull(float x, float y) {
        shapeRenderer.setColor(new Color(1.00f, 0.38f, 0.30f, 1f));
        shapeRenderer.circle(x, y, 26);
        shapeRenderer.rect(x - 17, y - 28, 34, 18);
        shapeRenderer.setColor(new Color(0.035f, 0.010f, 0.010f, 1f));
        shapeRenderer.circle(x - 10, y + 4, 6);
        shapeRenderer.circle(x + 10, y + 4, 6);
        shapeRenderer.triangle(x - 4, y - 8, x + 4, y - 8, x, y - 2);
        shapeRenderer.rect(x - 12, y - 23, 4, 9);
        shapeRenderer.rect(x - 2, y - 23, 4, 9);
        shapeRenderer.rect(x + 8, y - 23, 4, 9);
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (retryButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            gameFacade.restartLevel(levelNumber);
        } else if (menuButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            gameFacade.goToMainMenu();
        }
    }

    private void createButtons() {
        retryButton = new MenuButton("Retry", Main.SCREEN_WIDTH / 2f - 125, 288, 250, 50);
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 125, 220, 250, 50);
    }

    private float getUiMouseX() {
        return Gdx.input.getX() * (Main.SCREEN_WIDTH / (float) Gdx.graphics.getWidth());
    }

    private float getUiMouseY() {
        return Main.SCREEN_HEIGHT - Gdx.input.getY() * (Main.SCREEN_HEIGHT / (float) Gdx.graphics.getHeight());
    }

    @Override public void show() {
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().playGameOver();
    }
    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        createButtons();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
