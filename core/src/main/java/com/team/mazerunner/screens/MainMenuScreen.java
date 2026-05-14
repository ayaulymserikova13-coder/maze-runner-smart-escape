package com.team.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team.mazerunner.Main;

public class MainMenuScreen implements Screen {

    private final Main game;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private MenuButton playButton;
    private MenuButton quitButton;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        createButtons();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.035f, 0.040f, 0.050f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderMenuBackground();
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 230, 172, 460, 365);
        drawSideMazeRelic(122, 230);
        drawSideMazeRelic(Main.SCREEN_WIDTH - 186, 230);
        drawTitleOrnament();
        playButton.drawShape(shapeRenderer, playButton.contains(mouseX, mouseY));
        quitButton.drawShape(shapeRenderer, quitButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(new Color(0.58f, 1.00f, 0.94f, 1f));
        font.getData().setScale(2f);
        font.draw(game.batch, "MAZE RUNNER", Main.SCREEN_WIDTH / 2f - 135, 421);
        font.getData().setScale(1f);
        font.setColor(new Color(0.54f, 0.94f, 0.88f, 1f));
        font.draw(game.batch, "2D STEALTH MAZE ESCAPE", Main.SCREEN_WIDTH / 2f - 94, 380);
        playButton.drawText(game.batch, font);
        quitButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height) {
        shapeRenderer.setColor(new Color(0.003f, 0.006f, 0.008f, 1f));
        shapeRenderer.rect(x + 10, y - 10, width, height);
        shapeRenderer.setColor(new Color(0.04f, 0.98f, 0.92f, 1f));
        shapeRenderer.rect(x, y, width, 5);
        shapeRenderer.rect(x, y + height - 5, width, 5);
        shapeRenderer.rect(x, y, 5, height);
        shapeRenderer.rect(x + width - 5, y, 5, height);
        shapeRenderer.setColor(new Color(0.010f, 0.030f, 0.034f, 1f));
        shapeRenderer.rect(x + 5, y + 5, width - 10, height - 10);
        shapeRenderer.setColor(new Color(0.020f, 0.090f, 0.095f, 1f));
        for (float gx = x + 30; gx < x + width - 30; gx += 32) {
            shapeRenderer.rect(gx, y + 34, 1, height - 68);
        }
        for (float gy = y + 34; gy < y + height - 30; gy += 32) {
            shapeRenderer.rect(x + 28, gy, width - 56, 1);
        }
        shapeRenderer.setColor(new Color(0.38f, 0.96f, 0.88f, 1f));
        shapeRenderer.rect(x + 10, y + 10, 8, 8);
        shapeRenderer.rect(x + width - 18, y + 10, 8, 8);
        shapeRenderer.rect(x + 10, y + height - 18, 8, 8);
        shapeRenderer.rect(x + width - 18, y + height - 18, 8, 8);
        shapeRenderer.setColor(new Color(0.08f, 0.58f, 0.54f, 1f));
        shapeRenderer.rect(x + 24, y + height - 25, width - 48, 2);
        shapeRenderer.rect(x + 24, y + 28, width - 48, 2);
    }

    private void drawSideMazeRelic(float x, float y) {
        shapeRenderer.setColor(new Color(0.012f, 0.014f, 0.016f, 1f));
        shapeRenderer.rect(x - 8, y - 8, 72, 212);
        shapeRenderer.setColor(new Color(0.075f, 0.115f, 0.120f, 1f));
        shapeRenderer.rect(x, y, 56, 196);
        shapeRenderer.setColor(new Color(0.68f, 0.46f, 0.24f, 1f));
        shapeRenderer.rect(x, y, 56, 5);
        shapeRenderer.rect(x, y + 191, 56, 5);
        shapeRenderer.rect(x, y, 5, 196);
        shapeRenderer.rect(x + 51, y, 5, 196);

        shapeRenderer.setColor(new Color(0.18f, 0.36f, 0.34f, 1f));
        shapeRenderer.rect(x + 14, y + 26, 28, 8);
        shapeRenderer.rect(x + 14, y + 26, 8, 46);
        shapeRenderer.rect(x + 14, y + 64, 30, 8);
        shapeRenderer.rect(x + 36, y + 64, 8, 42);
        shapeRenderer.rect(x + 18, y + 98, 26, 8);
        shapeRenderer.rect(x + 18, y + 98, 8, 44);
        shapeRenderer.rect(x + 18, y + 134, 24, 8);

        shapeRenderer.setColor(new Color(0.38f, 0.68f, 0.62f, 1f));
        shapeRenderer.rect(x + 23, y + 152, 10, 10);
        shapeRenderer.rect(x + 25, y + 154, 6, 6);
    }

    private void drawTitleOrnament() {
        float centerX = Main.SCREEN_WIDTH / 2f;
        shapeRenderer.setColor(new Color(0.05f, 0.86f, 0.82f, 1f));
        shapeRenderer.rect(centerX - 250, 532, 132, 3);
        shapeRenderer.rect(centerX + 118, 532, 132, 3);
        shapeRenderer.rect(centerX - 115, 508, 230, 3);
        shapeRenderer.rect(centerX - 5, 484, 10, 24);
        shapeRenderer.setColor(new Color(0.04f, 0.22f, 0.24f, 1f));
        shapeRenderer.rect(centerX - 28, 462, 56, 28);
        shapeRenderer.setColor(new Color(0.45f, 1.00f, 0.90f, 1f));
        shapeRenderer.rect(centerX - 17, 470, 34, 12);
        shapeRenderer.setColor(new Color(0.010f, 0.050f, 0.055f, 1f));
        shapeRenderer.rect(centerX - 10, 472, 6, 6);
        shapeRenderer.rect(centerX + 4, 472, 6, 6);
    }

    private void renderMenuBackground() {
        shapeRenderer.setColor(new Color(0.035f, 0.043f, 0.050f, 1f));
        shapeRenderer.rect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        shapeRenderer.setColor(new Color(0.115f, 0.125f, 0.125f, 1f));

        for (int i = 0; i < 9; i++) {
            float y = 80 + i * 66;
            shapeRenderer.rect(80, y, Main.SCREEN_WIDTH - 160, 34);
            shapeRenderer.setColor(new Color(0.026f, 0.030f, 0.034f, 1f));
            shapeRenderer.rect(80, y, Main.SCREEN_WIDTH - 160, 4);
            shapeRenderer.rect(80 + (i % 2) * 90, y + 30, 240, 4);
            shapeRenderer.setColor(new Color(0.115f, 0.125f, 0.125f, 1f));
        }

        shapeRenderer.setColor(new Color(0.018f, 0.024f, 0.030f, 1f));
        shapeRenderer.rect(0, 0, Main.SCREEN_WIDTH, 76);
        shapeRenderer.rect(0, Main.SCREEN_HEIGHT - 76, Main.SCREEN_WIDTH, 76);
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (playButton.contains(mouseX, mouseY)) {
            game.setScreen(new GameScreen(game));
        } else if (quitButton.contains(mouseX, mouseY)) {
            Gdx.app.exit();
        }

    }

    private void createButtons() {
        this.playButton = new MenuButton("Play", Main.SCREEN_WIDTH / 2f - 155, 306, 310, 54);
        this.quitButton = new MenuButton("Quit", Main.SCREEN_WIDTH / 2f - 155, 236, 310, 54);
    }

    private float getUiMouseX() {
        return Gdx.input.getX() * (Main.SCREEN_WIDTH / (float) Gdx.graphics.getWidth());
    }

    private float getUiMouseY() {
        return Main.SCREEN_HEIGHT - Gdx.input.getY() * (Main.SCREEN_HEIGHT / (float) Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        createButtons();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
