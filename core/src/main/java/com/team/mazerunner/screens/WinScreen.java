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
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 210, 195, 420, 308, new Color(0.24f, 0.58f, 0.56f, 1f));
        menuButton.drawShape(shapeRenderer, menuButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.setColor(new Color(0.90f, 1f, 0.94f, 1f));
        font.getData().setScale(1.8f);
        font.draw(game.batch, "YOU ESCAPED!", Main.SCREEN_WIDTH / 2f - 120, 430);
        font.getData().setScale(1f);
        font.setColor(new Color(0.70f, 0.88f, 0.80f, 1f));
        font.draw(game.batch, "All maze levels completed.", Main.SCREEN_WIDTH / 2f - 88, 390);
        menuButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height, Color accentColor) {
        shapeRenderer.setColor(new Color(0.010f, 0.014f, 0.012f, 1f));
        shapeRenderer.rect(x + 8, y - 8, width, height);
        shapeRenderer.setColor(new Color(0.68f, 0.46f, 0.24f, 1f));
        shapeRenderer.rect(x, y, width, 6);
        shapeRenderer.rect(x, y + height - 6, width, 6);
        shapeRenderer.rect(x, y, 6, height);
        shapeRenderer.rect(x + width - 6, y, 6, height);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 12, y + height - 18, width - 24, 4);
        shapeRenderer.setColor(new Color(0.018f, 0.026f, 0.024f, 1f));
        shapeRenderer.rect(x + 6, y + 6, width - 12, height - 12);
        shapeRenderer.setColor(new Color(0.68f, 0.46f, 0.24f, 1f));
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

        if (menuButton.contains(mouseX, mouseY)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void createButtons() {
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 110, 250, 220, 54);
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
