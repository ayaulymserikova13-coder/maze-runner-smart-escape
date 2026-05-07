package com.team.mazerunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.team.mazerunner.Main;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.input.PlayerInputHandler;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;
import com.team.mazerunner.world.LevelMap;

public class GameScreen implements Screen {

    private final Main game;

    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Player player;
    private LevelMap levelMap;
    private PlayerInputHandler inputHandler;
    private boolean levelComplete;
    private boolean paused;
    private MenuButton resumeButton;
    private MenuButton restartButton;
    private MenuButton menuButton;

    public GameScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        loadLevel(1);
        createPauseButtons();
    }

    public GameScreen(Main game, int levelNumber) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        loadLevel(levelNumber);
        createPauseButtons();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.08f, 0.08f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderMap();
        player.render(shapeRenderer);

        shapeRenderer.end();

        renderHud();

        if (paused) {
            renderPauseOverlay();
        }
    }

    private void update(float delta) {
        if (levelComplete) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        if (paused) {
            handlePauseButtons();
            return;
        }

        inputHandler.update(delta);
        levelMap.update(delta, player);

        if (player.isDead()) {
            game.setScreen(new GameOverScreen(game, levelMap.getLevelNumber()));
            return;
        }

        camera.position.set(
                player.getX(),
                player.getY(),
                0
        );

        if (levelMap.isExitReached(player)) {
            if (levelMap.isFinalLevel()) {
                game.setScreen(new WinScreen(game));
            } else {
                loadLevel(levelMap.getLevelNumber() + 1);
            }
        }
    }

    private void renderMap() {
        levelMap.render(shapeRenderer);
    }

    private void renderHud() {
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawPixelPanel(16, Main.SCREEN_HEIGHT - 72, 220, 52, new Color(0.68f, 0.46f, 0.24f, 1f));
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 125, Main.SCREEN_HEIGHT - 66, 250, 42, new Color(0.24f, 0.58f, 0.56f, 1f));
        drawPixelPanel(Main.SCREEN_WIDTH - 292, Main.SCREEN_HEIGHT - 76, 276, 64, new Color(0.68f, 0.46f, 0.24f, 1f));
        drawPixelPanel(18, 18, 610, 42, new Color(0.24f, 0.58f, 0.56f, 1f));
        renderHealthIcons();
        renderInventorySlots();
        shapeRenderer.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();

        font.setColor(new Color(0.82f, 0.68f, 0.46f, 1f));
        font.draw(game.batch, "HP", 32, Main.SCREEN_HEIGHT - 35);
        font.draw(game.batch, "LEVEL " + levelMap.getLevelNumber(), Main.SCREEN_WIDTH / 2f - 34, Main.SCREEN_HEIGHT - 39);
        font.draw(game.batch, "ITEMS", Main.SCREEN_WIDTH - 278, Main.SCREEN_HEIGHT - 29);
        font.setColor(new Color(0.83f, 0.90f, 0.84f, 1f));
        font.draw(game.batch, levelMap.getStatusMessage(), 32, 43);
        font.setColor(new Color(0.48f, 0.78f, 0.72f, 1f));
        font.draw(game.batch, "1 KEY   2 CROWBAR   3 KNIFE", Main.SCREEN_WIDTH - 280, Main.SCREEN_HEIGHT - 63);
        font.setColor(new Color(0.70f, 0.62f, 0.52f, 1f));
        font.draw(game.batch, "E use   F kill   Esc pause", Main.SCREEN_WIDTH - 224, 43);

        game.batch.end();
    }

    private void drawPixelPanel(float x, float y, float width, float height, Color borderColor) {
        shapeRenderer.setColor(new Color(0.010f, 0.012f, 0.014f, 1f));
        shapeRenderer.rect(x + 6, y - 6, width, height);
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(x, y, width, 5);
        shapeRenderer.rect(x, y + height - 5, width, 5);
        shapeRenderer.rect(x, y, 5, height);
        shapeRenderer.rect(x + width - 5, y, 5, height);
        shapeRenderer.setColor(new Color(0.018f, 0.023f, 0.026f, 1f));
        shapeRenderer.rect(x + 5, y + 5, width - 10, height - 10);
        shapeRenderer.setColor(new Color(0.25f, 0.58f, 0.56f, 1f));
        shapeRenderer.rect(x + 8, y + 8, 7, 7);
        shapeRenderer.rect(x + width - 15, y + 8, 7, 7);
        shapeRenderer.rect(x + 8, y + height - 15, 7, 7);
        shapeRenderer.rect(x + width - 15, y + height - 15, 7, 7);
    }

    private void renderHealthIcons() {
        for (int i = 0; i < 3; i++) {
            float iconX = 70 + i * 30;
            float iconY = Main.SCREEN_HEIGHT - 50;

            shapeRenderer.setColor(i < player.getHp()
                    ? new Color(0.76f, 0.11f, 0.16f, 1f)
                    : new Color(0.13f, 0.14f, 0.16f, 1f));
            shapeRenderer.rect(iconX + 4, iconY, 12, 12);
            shapeRenderer.rect(iconX, iconY + 4, 20, 8);
            if (i < player.getHp()) {
                shapeRenderer.setColor(new Color(0.94f, 0.35f, 0.41f, 1f));
                shapeRenderer.rect(iconX + 5, iconY + 9, 5, 3);
            }
        }
    }

    private void renderInventorySlots() {
        drawInventorySlot(Main.SCREEN_WIDTH - 172, Main.SCREEN_HEIGHT - 54, Key.TYPE, new Color(0.92f, 0.63f, 0.12f, 1f));
        drawInventorySlot(Main.SCREEN_WIDTH - 120, Main.SCREEN_HEIGHT - 54, Crowbar.TYPE, new Color(0.64f, 0.31f, 0.15f, 1f));
        drawInventorySlot(Main.SCREEN_WIDTH - 68, Main.SCREEN_HEIGHT - 54, Knife.TYPE, new Color(0.72f, 0.78f, 0.82f, 1f));
    }

    private void drawInventorySlot(float x, float y, String itemType, Color itemColor) {
        boolean hasItem = player.hasItem(itemType);
        boolean active = itemType.equals(player.getActiveItem());

        if (active) {
            shapeRenderer.setColor(new Color(0.60f, 0.78f, 0.64f, 1f));
            shapeRenderer.rect(x - 5, y - 5, 44, 44);
        }
        shapeRenderer.setColor(new Color(0.012f, 0.016f, 0.020f, 1f));
        shapeRenderer.rect(x - 2, y - 2, 38, 38);
        shapeRenderer.setColor(hasItem ? new Color(0.12f, 0.12f, 0.10f, 1f) : new Color(0.045f, 0.042f, 0.040f, 1f));
        shapeRenderer.rect(x, y, 34, 34);
        shapeRenderer.setColor(hasItem ? new Color(0.48f, 0.72f, 0.66f, 1f) : new Color(0.18f, 0.15f, 0.12f, 1f));
        shapeRenderer.rect(x + 2, y + 30, 30, 2);
        shapeRenderer.rect(x + 2, y + 2, 2, 30);
        shapeRenderer.setColor(new Color(0.020f, 0.026f, 0.030f, 1f));
        shapeRenderer.rect(x + 7, y + 7, 20, 20);

        if (!hasItem) {
            return;
        }

        shapeRenderer.setColor(itemColor);
        if (Key.TYPE.equals(itemType)) {
            shapeRenderer.circle(x + 12, y + 20, 5);
            shapeRenderer.rect(x + 16, y + 18, 12, 4);
            shapeRenderer.rect(x + 25, y + 13, 3, 5);
            shapeRenderer.rect(x + 21, y + 13, 3, 5);
            shapeRenderer.setColor(new Color(1f, 0.84f, 0.34f, 1f));
            shapeRenderer.rect(x + 17, y + 22, 8, 1);
        } else if (Crowbar.TYPE.equals(itemType)) {
            shapeRenderer.setColor(new Color(0.13f, 0.06f, 0.05f, 1f));
            shapeRenderer.rect(x + 11, y + 7, 8, 22);
            shapeRenderer.rect(x + 17, y + 24, 10, 6);
            shapeRenderer.rect(x + 23, y + 20, 4, 5);
            shapeRenderer.setColor(itemColor);
            shapeRenderer.rect(x + 13, y + 8, 4, 19);
            shapeRenderer.rect(x + 18, y + 26, 7, 2);
        } else if (Knife.TYPE.equals(itemType)) {
            shapeRenderer.setColor(new Color(0.035f, 0.040f, 0.045f, 1f));
            shapeRenderer.rect(x + 13, y + 6, 7, 10);
            shapeRenderer.rect(x + 9, y + 14, 15, 5);
            shapeRenderer.triangle(x + 10, y + 17, x + 24, y + 17, x + 18, y + 30);
            shapeRenderer.setColor(new Color(0.42f, 0.10f, 0.12f, 1f));
            shapeRenderer.rect(x + 14, y + 7, 5, 8);
            shapeRenderer.setColor(new Color(0.78f, 0.50f, 0.24f, 1f));
            shapeRenderer.rect(x + 12, y + 14, 11, 3);
            shapeRenderer.setColor(itemColor);
            shapeRenderer.triangle(x + 11, y + 18, x + 23, y + 18, x + 18, y + 29);
            shapeRenderer.setColor(new Color(0.96f, 0.98f, 1f, 1f));
            shapeRenderer.triangle(x + 16, y + 19, x + 22, y + 19, x + 18, y + 28);
        }
    }

    private void renderPauseOverlay() {
        float mouseX = Gdx.input.getX();
        float mouseY = Main.SCREEN_HEIGHT - Gdx.input.getY();

        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.04f, 0.06f, 0.08f, 1f));
        shapeRenderer.rect(Main.SCREEN_WIDTH / 2f - 180, 200, 360, 290);
        shapeRenderer.setColor(new Color(0.18f, 0.36f, 0.42f, 1f));
        shapeRenderer.rect(Main.SCREEN_WIDTH / 2f - 180, 486, 360, 4);
        resumeButton.drawShape(shapeRenderer, resumeButton.contains(mouseX, mouseY));
        restartButton.drawShape(shapeRenderer, restartButton.contains(mouseX, mouseY));
        menuButton.drawShape(shapeRenderer, menuButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        font.draw(game.batch, "PAUSED", Main.SCREEN_WIDTH / 2f - 50, 445);
        font.getData().setScale(1f);
        resumeButton.drawText(game.batch, font);
        restartButton.drawText(game.batch, font);
        menuButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void handlePauseButtons() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = Gdx.input.getX();
        float mouseY = Main.SCREEN_HEIGHT - Gdx.input.getY();

        if (resumeButton.contains(mouseX, mouseY)) {
            paused = false;
        } else if (restartButton.contains(mouseX, mouseY)) {
            loadLevel(levelMap.getLevelNumber());
            paused = false;
        } else if (menuButton.contains(mouseX, mouseY)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void loadLevel(int levelNumber) {
        levelMap = new LevelMap(levelNumber);
        player = new Player(levelMap.getSpawnX(), levelMap.getSpawnY());
        inputHandler = new PlayerInputHandler(player, levelMap);
        levelComplete = false;
    }

    private void createPauseButtons() {
        resumeButton = new MenuButton("Resume", Main.SCREEN_WIDTH / 2f - 110, 350, 220, 48);
        restartButton = new MenuButton("Restart Level", Main.SCREEN_WIDTH / 2f - 110, 290, 220, 48);
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 110, 230, 220, 48);
    }

    @Override
    public void resize(int width, int height) {

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
