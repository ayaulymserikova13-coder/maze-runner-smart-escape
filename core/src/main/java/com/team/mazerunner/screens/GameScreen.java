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
import com.team.mazerunner.audio.AudioManager;
import com.team.mazerunner.entities.HPObserver;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.input.PlayerInputHandler;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Disguise;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;
import com.team.mazerunner.world.LevelMap;

public class GameScreen implements Screen, HPObserver {

    private final Main game;
    private final GameFacade gameFacade;

    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Player player;
    private LevelMap levelMap;
    private PlayerInputHandler inputHandler;
    private boolean levelComplete;
    private boolean paused;
    private boolean gameOverRequested;
    private int observedHp = Player.MAX_HP;
    private int nextLevelNumber;
    private MenuButton resumeButton;
    private MenuButton restartButton;
    private MenuButton menuButton;
    private MenuButton continueButton;
    private MenuButton exitButton;

    public GameScreen(Main game) {
        this.game = game;
        this.gameFacade = new GameFacade(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        loadLevel(1);
        createPauseButtons();
        createLevelCompleteButtons();
    }

    public GameScreen(Main game, int levelNumber) {
        this.game = game;
        this.gameFacade = new GameFacade(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        loadLevel(levelNumber);
        createPauseButtons();
        createLevelCompleteButtons();
    }

    @Override
    public void show() {
        AudioManager.getInstance().playGameMusic();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.060f, 0.064f, 0.070f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        renderLevelBackground();
        renderMap();
        player.render(shapeRenderer);

        shapeRenderer.end();

        renderHud();

        if (paused) {
            renderPauseOverlay();
        }

        if (levelComplete) {
            renderLevelCompleteOverlay();
        }
    }

    private void update(float delta) {
        if (levelComplete) {
            handleLevelCompleteButtons();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            AudioManager.getInstance().toggleMusic();
        }

        if (paused) {
            handlePauseButtons();
            return;
        }

        inputHandler.update(delta);
        player.update(delta);
        levelMap.update(delta, player);

        if (gameOverRequested || player.isDead()) {
            gameFacade.showGameOver(levelMap.getLevelNumber());
            return;
        }

        updateCameraPosition();

        if (levelMap.isExitReached(player)) {
            AudioManager.getInstance().playSuccess();
            if (levelMap.isFinalLevel()) {
                gameFacade.showWin();
            } else {
                nextLevelNumber = levelMap.getLevelNumber() + 1;
                levelComplete = true;
            }
        }
    }

    private void renderMap() {
        levelMap.render(shapeRenderer, player);
    }

    private void renderLevelBackground() {
        float left = camera.position.x - Main.SCREEN_WIDTH / 2f;
        float bottom = camera.position.y - Main.SCREEN_HEIGHT / 2f;
        float width = Main.SCREEN_WIDTH;
        float height = Main.SCREEN_HEIGHT;
        int level = levelMap.getLevelNumber();

        if (level == 1) {
            drawIndustrialBase(left, bottom, width, height,
                    new Color(0.025f, 0.036f, 0.038f, 1f),
                    new Color(0.055f, 0.082f, 0.080f, 1f),
                    new Color(0.10f, 0.30f, 0.25f, 1f));
            drawFacilityVents(left, bottom, width, height);
        } else if (level == 2) {
            drawIndustrialBase(left, bottom, width, height,
                    new Color(0.022f, 0.028f, 0.035f, 1f),
                    new Color(0.050f, 0.065f, 0.080f, 1f),
                    new Color(0.22f, 0.06f, 0.07f, 1f));
            drawSecurityPanels(left, bottom, width, height);
        } else if (level == 3) {
            drawIndustrialBase(left, bottom, width, height,
                    new Color(0.042f, 0.032f, 0.024f, 1f),
                    new Color(0.084f, 0.058f, 0.034f, 1f),
                    new Color(0.30f, 0.18f, 0.07f, 1f));
            drawWarehouseSilhouettes(left, bottom, width, height);
        } else {
            drawIndustrialBase(left, bottom, width, height,
                    new Color(0.025f, 0.026f, 0.038f, 1f),
                    new Color(0.055f, 0.060f, 0.088f, 1f),
                    new Color(0.08f, 0.26f, 0.32f, 1f));
            drawLaboratoryBackwall(left, bottom, width, height);
        }
    }

    private void drawIndustrialBase(float left, float bottom, float width, float height,
                                    Color baseColor, Color panelColor, Color accentColor) {
        shapeRenderer.setColor(baseColor);
        shapeRenderer.rect(left, bottom, width, height);

        float startX = (float) Math.floor(left / 160f) * 160f;
        float startY = (float) Math.floor(bottom / 112f) * 112f;

        for (float y = startY; y < bottom + height + 112f; y += 112f) {
            shapeRenderer.setColor(new Color(panelColor.r, panelColor.g, panelColor.b, 0.72f));
            shapeRenderer.rect(left, y + 34, width, 12);
            shapeRenderer.setColor(new Color(0.010f, 0.012f, 0.014f, 1f));
            shapeRenderer.rect(left, y + 46, width, 4);
        }

        for (float x = startX; x < left + width + 160f; x += 160f) {
            shapeRenderer.setColor(new Color(panelColor.r * 0.7f, panelColor.g * 0.7f, panelColor.b * 0.7f, 1f));
            shapeRenderer.rect(x, bottom, 5, height);
            shapeRenderer.setColor(new Color(accentColor.r, accentColor.g, accentColor.b, 0.82f));
            shapeRenderer.rect(x + 18, bottom + 30, 30, 4);
            shapeRenderer.rect(x + 18, bottom + height - 52, 30, 4);
        }
    }

    private void drawFacilityVents(float left, float bottom, float width, float height) {
        float startX = (float) Math.floor(left / 220f) * 220f;
        float startY = (float) Math.floor(bottom / 150f) * 150f;

        for (float x = startX; x < left + width + 220f; x += 220f) {
            for (float y = startY; y < bottom + height + 150f; y += 150f) {
                shapeRenderer.setColor(new Color(0.030f, 0.054f, 0.052f, 1f));
                shapeRenderer.rect(x + 48, y + 44, 58, 34);
                shapeRenderer.setColor(new Color(0.13f, 0.28f, 0.24f, 1f));
                shapeRenderer.rect(x + 55, y + 52, 44, 4);
                shapeRenderer.rect(x + 55, y + 62, 44, 4);
                shapeRenderer.rect(x + 55, y + 72, 44, 4);
            }
        }
    }

    private void drawSecurityPanels(float left, float bottom, float width, float height) {
        float startX = (float) Math.floor(left / 260f) * 260f;
        float startY = (float) Math.floor(bottom / 170f) * 170f;

        for (float x = startX; x < left + width + 260f; x += 260f) {
            for (float y = startY; y < bottom + height + 170f; y += 170f) {
                shapeRenderer.setColor(new Color(0.030f, 0.040f, 0.052f, 1f));
                shapeRenderer.rect(x + 66, y + 54, 80, 44);
                shapeRenderer.setColor(new Color(0.10f, 0.16f, 0.18f, 1f));
                shapeRenderer.rect(x + 74, y + 62, 42, 5);
                shapeRenderer.rect(x + 74, y + 74, 56, 5);
                shapeRenderer.setColor(new Color(0.42f, 0.08f, 0.08f, 1f));
                shapeRenderer.rect(x + 132, y + 84, 7, 7);
            }
        }
    }

    private void drawWarehouseSilhouettes(float left, float bottom, float width, float height) {
        float startX = (float) Math.floor(left / 240f) * 240f;
        float startY = (float) Math.floor(bottom / 160f) * 160f;

        for (float x = startX; x < left + width + 240f; x += 240f) {
            for (float y = startY; y < bottom + height + 160f; y += 160f) {
                shapeRenderer.setColor(new Color(0.070f, 0.046f, 0.026f, 1f));
                shapeRenderer.rect(x + 42, y + 36, 44, 42);
                shapeRenderer.rect(x + 91, y + 36, 44, 58);
                shapeRenderer.setColor(new Color(0.16f, 0.09f, 0.035f, 1f));
                shapeRenderer.rect(x + 48, y + 55, 32, 4);
                shapeRenderer.rect(x + 97, y + 62, 32, 4);
                shapeRenderer.setColor(new Color(0.34f, 0.18f, 0.06f, 1f));
                shapeRenderer.rect(x + 54, y + 42, 7, 7);
            }
        }
    }

    private void drawLaboratoryBackwall(float left, float bottom, float width, float height) {
        float startX = (float) Math.floor(left / 250f) * 250f;
        float startY = (float) Math.floor(bottom / 155f) * 155f;

        for (float x = startX; x < left + width + 250f; x += 250f) {
            for (float y = startY; y < bottom + height + 155f; y += 155f) {
                shapeRenderer.setColor(new Color(0.032f, 0.042f, 0.060f, 1f));
                shapeRenderer.rect(x + 58, y + 42, 86, 50);
                shapeRenderer.setColor(new Color(0.08f, 0.22f, 0.25f, 1f));
                shapeRenderer.rect(x + 68, y + 80, 56, 5);
                shapeRenderer.rect(x + 68, y + 58, 30, 5);
                shapeRenderer.setColor(new Color(0.36f, 0.06f, 0.08f, 1f));
                shapeRenderer.rect(x + 130, y + 58, 8, 18);
            }
        }
    }

    private void updateCameraPosition() {
        float halfWidth = Main.SCREEN_WIDTH / 2f;
        float halfHeight = Main.SCREEN_HEIGHT / 2f;
        float mapWidth = levelMap.getPixelWidth();
        float mapHeight = levelMap.getPixelHeight();
        float cameraX = player.getX();
        float cameraY = player.getY();

        if (mapWidth > Main.SCREEN_WIDTH) {
            cameraX = Math.max(halfWidth, Math.min(mapWidth - halfWidth, cameraX));
        } else {
            cameraX = mapWidth / 2f;
        }

        if (mapHeight > Main.SCREEN_HEIGHT) {
            cameraY = Math.max(halfHeight, Math.min(mapHeight - halfHeight, cameraY));
        } else {
            cameraY = mapHeight / 2f;
        }

        camera.position.set(cameraX, cameraY, 0);
    }

    private void renderHud() {
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawPixelPanel(16, Main.SCREEN_HEIGHT - 72, 220, 52, new Color(0.68f, 0.46f, 0.24f, 1f));
        drawPixelPanel(Main.SCREEN_WIDTH / 2f - 125, Main.SCREEN_HEIGHT - 66, 250, 42, new Color(0.24f, 0.58f, 0.56f, 1f));
        drawPixelPanel(Main.SCREEN_WIDTH - 344, Main.SCREEN_HEIGHT - 76, 328, 64, new Color(0.68f, 0.46f, 0.24f, 1f));
        drawPixelPanel(18, 18, 610, 42, new Color(0.24f, 0.58f, 0.56f, 1f));
        renderHealthIcons();
        renderInventorySlots();
        shapeRenderer.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();

        font.setColor(new Color(0.82f, 0.68f, 0.46f, 1f));
        font.draw(game.batch, "HP", 32, Main.SCREEN_HEIGHT - 35);
        font.draw(game.batch, "LEVEL " + levelMap.getLevelNumber(), Main.SCREEN_WIDTH / 2f - 34, Main.SCREEN_HEIGHT - 39);
        font.draw(game.batch, "ITEMS", Main.SCREEN_WIDTH - 330, Main.SCREEN_HEIGHT - 29);
        font.setColor(new Color(0.83f, 0.90f, 0.84f, 1f));
        font.draw(game.batch, levelMap.getStatusMessage(), 32, 43);
        font.setColor(new Color(0.48f, 0.78f, 0.72f, 1f));
        font.draw(game.batch, "1 KEY  2 CROWBAR  3 KNIFE  4 DISGUISE", Main.SCREEN_WIDTH - 336, Main.SCREEN_HEIGHT - 63);
        font.setColor(new Color(0.70f, 0.62f, 0.52f, 1f));
        font.draw(game.batch, "E use   F kill   Esc pause", Main.SCREEN_WIDTH - 224, 43);
        font.draw(game.batch, AudioManager.getInstance().isMusicMuted() ? "M music off" : "M music on",
                Main.SCREEN_WIDTH - 224, 24);
        if (player.isDisguised()) {
            font.setColor(new Color(0.58f, 0.86f, 0.78f, 1f));
            font.draw(game.batch, "DISGUISE " + (int) Math.ceil(player.getDisguiseTimeRemaining()) + "s", 190, Main.SCREEN_HEIGHT - 35);
        }

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

            shapeRenderer.setColor(i < observedHp
                    ? new Color(0.76f, 0.11f, 0.16f, 1f)
                    : new Color(0.13f, 0.14f, 0.16f, 1f));
            shapeRenderer.rect(iconX + 4, iconY, 12, 12);
            shapeRenderer.rect(iconX, iconY + 4, 20, 8);
            if (i < observedHp) {
                shapeRenderer.setColor(new Color(0.94f, 0.35f, 0.41f, 1f));
                shapeRenderer.rect(iconX + 5, iconY + 9, 5, 3);
            }
        }
    }

    private void renderInventorySlots() {
        drawInventorySlot(Main.SCREEN_WIDTH - 224, Main.SCREEN_HEIGHT - 54, Key.TYPE, new Color(0.92f, 0.63f, 0.12f, 1f));
        drawInventorySlot(Main.SCREEN_WIDTH - 172, Main.SCREEN_HEIGHT - 54, Crowbar.TYPE, new Color(0.64f, 0.31f, 0.15f, 1f));
        drawInventorySlot(Main.SCREEN_WIDTH - 120, Main.SCREEN_HEIGHT - 54, Knife.TYPE, new Color(0.72f, 0.78f, 0.82f, 1f));
        drawInventorySlot(Main.SCREEN_WIDTH - 68, Main.SCREEN_HEIGHT - 54, Disguise.TYPE, new Color(0.52f, 0.72f, 0.68f, 1f));
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
        } else if (Disguise.TYPE.equals(itemType)) {
            shapeRenderer.setColor(new Color(0.08f, 0.07f, 0.12f, 1f));
            shapeRenderer.rect(x + 9, y + 9, 16, 16);
            shapeRenderer.circle(x + 17, y + 23, 6);
            shapeRenderer.setColor(itemColor);
            shapeRenderer.rect(x + 11, y + 11, 12, 12);
            shapeRenderer.circle(x + 17, y + 22, 5);
            shapeRenderer.setColor(new Color(0.06f, 0.055f, 0.08f, 1f));
            shapeRenderer.rect(x + 12, y + 19, 10, 3);
            shapeRenderer.setColor(new Color(0.84f, 0.95f, 0.88f, 1f));
            shapeRenderer.rect(x + 13, y + 22, 2, 2);
            shapeRenderer.rect(x + 20, y + 22, 2, 2);
        }
    }

    private void renderPauseOverlay() {
        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

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

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (resumeButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            paused = false;
        } else if (restartButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            gameFacade.restartLevel(levelMap.getLevelNumber());
            paused = false;
        } else if (menuButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            gameFacade.goToMainMenu();
        }
    }

    private void renderLevelCompleteOverlay() {
        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderLevelCompleteBackground();
        drawLevelCompletePanel(Main.SCREEN_WIDTH / 2f - 225, 168, 450, 335);
        drawStarIcon(Main.SCREEN_WIDTH / 2f, 446);
        continueButton.drawShape(shapeRenderer, continueButton.contains(mouseX, mouseY));
        exitButton.drawShape(shapeRenderer, exitButton.contains(mouseX, mouseY));
        shapeRenderer.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        font.setColor(new Color(1.00f, 0.78f, 0.22f, 1f));
        font.getData().setScale(1.7f);
        font.draw(game.batch, "LEVEL COMPLETE", Main.SCREEN_WIDTH / 2f - 122, 405);
        font.getData().setScale(1f);
        font.setColor(new Color(1.00f, 0.84f, 0.38f, 1f));
        font.draw(game.batch, "Ready for level " + nextLevelNumber, Main.SCREEN_WIDTH / 2f - 72, 366);
        continueButton.drawText(game.batch, font);
        exitButton.drawText(game.batch, font);
        game.batch.end();
    }

    private void handleLevelCompleteButtons() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = getUiMouseX();
        float mouseY = getUiMouseY();

        if (continueButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            loadLevel(nextLevelNumber);
        } else if (exitButton.contains(mouseX, mouseY)) {
            AudioManager.getInstance().playClick();
            gameFacade.goToMainMenu();
        }
    }

    private void loadLevel(int levelNumber) {
        levelMap = new LevelMap(levelNumber);
        player = new Player(levelMap.getSpawnX(), levelMap.getSpawnY());
        player.addHPObserver(this);
        inputHandler = new PlayerInputHandler(player, levelMap);
        levelComplete = false;
        gameOverRequested = false;
        nextLevelNumber = levelNumber + 1;
    }

    @Override
    public void onHPChanged(int newHP) {
        observedHp = newHP;

        if (newHP <= 0) {
            gameOverRequested = true;
        }
    }

    private void createPauseButtons() {
        resumeButton = new MenuButton("Resume", Main.SCREEN_WIDTH / 2f - 110, 352, 220, 48);
        restartButton = new MenuButton("Restart Level", Main.SCREEN_WIDTH / 2f - 110, 284, 220, 48);
        menuButton = new MenuButton("Main Menu", Main.SCREEN_WIDTH / 2f - 110, 216, 220, 48);
    }

    private void createLevelCompleteButtons() {
        continueButton = new MenuButton("Continue", Main.SCREEN_WIDTH / 2f - 130, 288, 260, 50);
        exitButton = new MenuButton("Exit", Main.SCREEN_WIDTH / 2f - 130, 220, 260, 50);
    }

    private void renderLevelCompleteBackground() {
        shapeRenderer.setColor(new Color(0.018f, 0.014f, 0.006f, 1f));
        shapeRenderer.rect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        drawHudGrid(70, 55, Main.SCREEN_WIDTH - 140, Main.SCREEN_HEIGHT - 110,
                new Color(0.13f, 0.075f, 0.020f, 1f), 30f);
    }

    private void drawLevelCompletePanel(float x, float y, float width, float height) {
        Color accent = new Color(1.00f, 0.68f, 0.08f, 1f);
        shapeRenderer.setColor(new Color(0.006f, 0.005f, 0.004f, 1f));
        shapeRenderer.rect(x + 10, y - 10, width, height);
        shapeRenderer.setColor(accent);
        shapeRenderer.rect(x, y, width, 5);
        shapeRenderer.rect(x, y + height - 5, width, 5);
        shapeRenderer.rect(x, y, 5, height);
        shapeRenderer.rect(x + width - 5, y, 5, height);
        shapeRenderer.setColor(new Color(0.030f, 0.022f, 0.012f, 1f));
        shapeRenderer.rect(x + 5, y + 5, width - 10, height - 10);
        drawHudGrid(x + 28, y + 36, width - 56, height - 72,
                new Color(0.11f, 0.070f, 0.020f, 1f), 28f);
        shapeRenderer.setColor(accent);
        shapeRenderer.rect(x + 18, y + height - 24, width - 36, 2);
        shapeRenderer.rect(x + 18, y + 30, width - 36, 2);
        shapeRenderer.rect(x + 10, y + 10, 8, 8);
        shapeRenderer.rect(x + width - 18, y + 10, 8, 8);
        shapeRenderer.rect(x + 10, y + height - 18, 8, 8);
        shapeRenderer.rect(x + width - 18, y + height - 18, 8, 8);
    }

    private void drawHudGrid(float x, float y, float width, float height, Color color, float step) {
        shapeRenderer.setColor(color);
        for (float gx = x; gx < x + width; gx += step) {
            shapeRenderer.rect(gx, y, 1, height);
        }
        for (float gy = y; gy < y + height; gy += step) {
            shapeRenderer.rect(x, gy, width, 1);
        }
    }

    private void drawStarIcon(float x, float y) {
        shapeRenderer.setColor(new Color(1.00f, 0.78f, 0.18f, 1f));
        shapeRenderer.triangle(x, y + 31, x - 9, y + 7, x + 9, y + 7);
        shapeRenderer.triangle(x - 29, y + 8, x - 5, y + 6, x - 18, y - 14);
        shapeRenderer.triangle(x + 29, y + 8, x + 5, y + 6, x + 18, y - 14);
        shapeRenderer.triangle(x - 18, y - 27, x, y - 10, x + 18, y - 27);
        shapeRenderer.circle(x, y, 16);
        shapeRenderer.setColor(new Color(1.00f, 0.92f, 0.35f, 1f));
        shapeRenderer.circle(x, y + 3, 9);
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
        hudCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        createPauseButtons();
        createLevelCompleteButtons();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        AudioManager.getInstance().stopMusic();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
