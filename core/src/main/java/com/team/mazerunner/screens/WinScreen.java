package com.team.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team.mazerunner.Main;

public class WinScreen implements Screen {

    private final Main game;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private MenuButton menuButton;

    public WinScreen(Main game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        createButtons();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.026f, 0.048f, 0.048f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderEscapeBackground();
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 245, 185, 490, 330, new Color(0.16f, 0.92f, 0.62f, 1f));
        drawUnlockedIcon(Main.SCREEN_WIDTH / 2f, 445);
        menuButton.drawShape(shapeRenderer, menuButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(new Color(0.40f, 1.00f, 0.72f, 1f));
        font.getData().setScale(1.8f);
        font.draw(game.batch, "YOU ESCAPED!", Main.SCREEN_WIDTH / 2f - 120, 405);
        font.getData().setScale(1f);
        font.setColor(new Color(0.54f, 0.96f, 0.78f, 1f));
        font.draw(game.batch, "All maze levels completed.", Main.SCREEN_WIDTH / 2f - 88, 366);
        menuButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height, Color accentColor) {
        shapeRenderer.setColor(new Color(0.004f, 0.010f, 0.008f, 1f));
        shapeRenderer.rect(x + 10, y - 10, width, height);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x, y, width, 5);
        shapeRenderer.rect(x, y + height - 5, width, 5);
        shapeRenderer.rect(x, y, 5, height);
        shapeRenderer.rect(x + width - 5, y, 5, height);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 20, y + height - 24, width - 40, 2);
        shapeRenderer.rect(x + 20, y + 28, width - 40, 2);
        shapeRenderer.setColor(new Color(0.010f, 0.032f, 0.024f, 1f));
        shapeRenderer.rect(x + 6, y + 6, width - 12, height - 12);
        drawPanelGrid(x + 25, y + 38, width - 50, height - 76, new Color(0.020f, 0.11f, 0.075f, 1f));
        shapeRenderer.setColor(new Color(0.44f, 1.00f, 0.75f, 1f));
        shapeRenderer.rect(x + 10, y + 10, 8, 8);
        shapeRenderer.rect(x + width - 18, y + 10, 8, 8);
        shapeRenderer.rect(x + 10, y + height - 18, 8, 8);
        shapeRenderer.rect(x + width - 18, y + height - 18, 8, 8);
    }

    private void renderEscapeBackground() {
        shapeRenderer.setColor(new Color(0.010f, 0.020f, 0.018f, 1f));
        shapeRenderer.rect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        drawPanelGrid(60, 60, Main.SCREEN_WIDTH - 120, Main.SCREEN_HEIGHT - 120,
                new Color(0.015f, 0.085f, 0.060f, 1f));

        shapeRenderer.setColor(new Color(0.02f, 0.20f, 0.12f, 1f));
        for (int i = 0; i < 42; i++) {
            float x = 80 + (i * 73) % (Main.SCREEN_WIDTH - 160);
            float y = 80 + (i * 41) % (Main.SCREEN_HEIGHT - 160);
            shapeRenderer.rect(x, y, 4, 4);
        }
    }

    private void drawPanelGrid(float x, float y, float width, float height, Color color) {
        shapeRenderer.setColor(color);
        for (float gx = x; gx < x + width; gx += 28) {
            shapeRenderer.rect(gx, y, 1, height);
        }
        for (float gy = y; gy < y + height; gy += 28) {
            shapeRenderer.rect(x, gy, width, 1);
        }
    }

    private void drawUnlockedIcon(float x, float y) {
        shapeRenderer.setColor(new Color(0.38f, 1.00f, 0.72f, 1f));
        shapeRenderer.rect(x - 24, y - 22, 48, 35);
        shapeRenderer.rect(x - 18, y + 10, 8, 22);
        shapeRenderer.rect(x - 18, y + 28, 36, 8);
        shapeRenderer.rect(x + 10, y + 18, 8, 18);
        shapeRenderer.setColor(new Color(0.012f, 0.045f, 0.035f, 1f));
        shapeRenderer.rect(x - 12, y - 14, 24, 17);
        shapeRenderer.circle(x, y - 2, 5);
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (menuButton.contains(mouseX, mouseY)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void createButtons() {
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 130, 248, 260, 54);
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
