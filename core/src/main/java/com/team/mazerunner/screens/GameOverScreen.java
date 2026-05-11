package com.team.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team.mazerunner.Main;

public class GameOverScreen implements Screen {

    private final Main game;
    private final int levelNumber;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private MenuButton retryButton;
    private MenuButton menuButton;

    public GameOverScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
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
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 210, 190, 420, 320, new Color(0.45f, 0.15f, 0.14f, 1f));
        retryButton.drawShape(shapeRenderer, retryButton.contains(mouseX, mouseY));
        menuButton.drawShape(shapeRenderer, menuButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(new Color(0.98f, 0.92f, 0.90f, 1f));
        font.getData().setScale(1.8f);
        font.draw(game.batch, "GAME OVER", Main.SCREEN_WIDTH / 2f - 95, 430);
        font.getData().setScale(1f);
        font.setColor(new Color(0.82f, 0.70f, 0.70f, 1f));
        font.draw(game.batch, "You were caught.", Main.SCREEN_WIDTH / 2f - 58, 390);
        retryButton.drawText(game.batch, font);
        menuButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height, Color accentColor) {
        shapeRenderer.setColor(new Color(0.010f, 0.008f, 0.008f, 1f));
        shapeRenderer.rect(x + 8, y - 8, width, height);
        shapeRenderer.setColor(new Color(0.68f, 0.46f, 0.24f, 1f));
        shapeRenderer.rect(x, y, width, 6);
        shapeRenderer.rect(x, y + height - 6, width, 6);
        shapeRenderer.rect(x, y, 6, height);
        shapeRenderer.rect(x + width - 6, y, 6, height);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 12, y + height - 18, width - 24, 4);
        shapeRenderer.setColor(new Color(0.020f, 0.018f, 0.018f, 1f));
        shapeRenderer.rect(x + 6, y + 6, width - 12, height - 12);
        shapeRenderer.setColor(new Color(0.25f, 0.58f, 0.56f, 1f));
        shapeRenderer.rect(x + 10, y + 10, 8, 8);
        shapeRenderer.rect(x + width - 18, y + 10, 8, 8);
        shapeRenderer.rect(x + 10, y + height - 18, 8, 8);
        shapeRenderer.rect(x + width - 18, y + height - 18, 8, 8);
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (retryButton.contains(mouseX, mouseY)) {
            game.setScreen(new GameScreen(game, levelNumber));
        } else if (menuButton.contains(mouseX, mouseY)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void createButtons() {
        retryButton = new MenuButton("Retry", Main.SCREEN_WIDTH / 2f - 110, 300, 220, 54);
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 110, 230, 220, 54);
    }

    private float getUiMouseX() {
        return Gdx.input.getX() * (Main.SCREEN_WIDTH / (float) Gdx.graphics.getWidth());
    }

    private float getUiMouseY() {
        return Main.SCREEN_HEIGHT - Gdx.input.getY() * (Main.SCREEN_HEIGHT / (float) Gdx.graphics.getHeight());
    }

    @Override public void show() {}
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
