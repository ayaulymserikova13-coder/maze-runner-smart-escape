package com.team.mazerunner.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team.mazerunner.enemies.Enemy;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.items.ItemFactory;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.List;

public class LevelMap {

    public static final int TILE_SIZE = 64;
    public static final int FLOOR = 0;
    public static final int WALL = 1;
    private static final Color WOODEN_DOOR_COLOR = new Color(0.45f, 0.25f, 0.08f, 1f);
    private static final Color IRON_DOOR_COLOR = new Color(0.42f, 0.46f, 0.5f, 1f);

    private final int levelNumber;
    private final int[][] map;
    private final float spawnX;
    private final float spawnY;

    private static final int[][] LEVEL_1 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1,1},
            {1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,1,1,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,1},
            {1,1,1,0,1,0,1,1,1,1,1,1,1,1,1,0,1,1},
            {1,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,1,1},
            {1,0,1,0,1,1,1,0,1,0,1,0,1,1,1,0,1,1},
            {1,0,1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,0,1,0,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1},
            {1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1},
            {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
            {1,0,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private static final int[][] LEVEL_2 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,0,0,1,0,1,0,1,1,1,0,1,0,1,1,1,1},
            {1,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,1,1},
            {1,0,1,1,1,1,1,1,1,0,1,0,1,1,1,0,1,1},
            {1,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,1},
            {1,0,0,1,1,0,1,0,1,1,0,1,0,0,1,0,1,1},
            {1,0,1,0,0,0,1,0,0,0,0,0,1,0,1,0,1,1},
            {1,0,1,0,1,1,1,0,1,1,1,0,0,0,1,0,1,1},
            {1,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,1,1},
            {1,1,0,1,1,0,1,0,1,0,1,1,1,0,1,0,1,1},
            {1,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,1,1},
            {1,0,1,1,1,1,1,0,1,1,0,0,1,1,1,0,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private static final int[][] LEVEL_3 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1},
            {1,1,1,1,1,1,1,0,1,0,1,1,1,1,1,0,1,1},
            {1,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,1,1},
            {1,0,1,0,1,1,1,1,0,0,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
            {1,0,0,0,1,0,1,1,1,1,1,1,1,0,1,0,1,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,1,0,1,0,1,1},
            {1,1,1,0,0,1,1,0,1,0,1,0,1,1,1,0,1,1},
            {1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,1,1},
            {1,0,1,0,1,1,1,0,1,0,1,0,1,0,1,0,1,1},
            {1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1},
            {1,0,1,1,1,1,1,0,1,0,1,0,1,1,1,0,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private static final int[][] LEVEL_4 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0,0,1,0,0,0,1},
            {1,0,1,0,1,0,1,1,0,1,0,1,1,1},
            {1,0,1,0,0,0,1,0,0,0,0,1,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,0,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,0,1},
            {1,1,1,0,1,1,1,0,1,0,1,1,0,1},
            {1,0,0,0,0,0,1,0,0,0,1,0,0,1},
            {1,0,1,1,1,0,1,1,1,0,1,0,1,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,0,1},
            {1,0,1,0,1,1,1,0,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private final List<ItemEntity> items = new ArrayList<>();
    private final List<DoorEntity> doors = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final Rectangle exitBounds;
    private String statusMessage = "Find the key, open the door, reach the exit.";
    private float damageCooldown;

    public LevelMap(int levelNumber) {
        this.levelNumber = levelNumber;
        this.map = createMap(levelNumber);
        this.spawnX = tileX(1) + 18;
        this.spawnY = tileY(1) + 18;
        this.exitBounds = createExitBounds(levelNumber);
        this.damageCooldown = 1.5f;

        createLevelObjects(levelNumber);
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == WALL) {
                    shapeRenderer.setColor(getWallColor());
                } else {
                    shapeRenderer.setColor(getFloorColor(row, col));
                }

                shapeRenderer.rect(
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );

                if (map[row][col] == WALL) {
                    renderWallDetail(shapeRenderer, col, row);
                } else {
                    renderFloorDetail(shapeRenderer, col, row);
                    renderFloorDecoration(shapeRenderer, col, row);
                }
            }
        }

        renderExitPortal(shapeRenderer);

        for (DoorEntity door : doors) {
            door.render(shapeRenderer);
        }

        for (ItemEntity item : items) {
            item.render(shapeRenderer);
        }

        for (Enemy enemy : enemies) {
            enemy.render(shapeRenderer);
        }
    }

    private Color getWallColor() {
        if (levelNumber == 1) {
            return new Color(0.19f, 0.24f, 0.25f, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.22f, 0.18f, 0.27f, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.31f, 0.24f, 0.16f, 1f);
        }

        return new Color(0.16f, 0.13f, 0.21f, 1f);
    }

    private void renderExitPortal(ShapeRenderer shapeRenderer) {
        float x = exitBounds.x;
        float y = exitBounds.y;

        shapeRenderer.setColor(new Color(0.006f, 0.018f, 0.022f, 1f));
        shapeRenderer.rect(x - 10, y - 10, exitBounds.width + 20, exitBounds.height + 20);
        shapeRenderer.setColor(new Color(0.045f, 0.18f, 0.20f, 1f));
        shapeRenderer.rect(x - 5, y - 5, exitBounds.width + 10, exitBounds.height + 10);

        shapeRenderer.setColor(new Color(0.18f, 0.55f, 0.53f, 1f));
        shapeRenderer.rect(x + 4, y + 4, exitBounds.width - 8, exitBounds.height - 8);
        shapeRenderer.setColor(new Color(0.46f, 0.88f, 0.76f, 1f));
        shapeRenderer.rect(x + 12, y + 10, exitBounds.width - 24, exitBounds.height - 20);
        shapeRenderer.setColor(new Color(0.025f, 0.075f, 0.105f, 1f));
        shapeRenderer.rect(x + 22, y + 18, exitBounds.width - 44, exitBounds.height - 36);

        renderPortalRunes(shapeRenderer, x, y);
        renderPortalGlow(shapeRenderer, x, y);
    }

    private void renderPortalRunes(ShapeRenderer shapeRenderer, float x, float y) {
        shapeRenderer.setColor(new Color(0.74f, 1f, 0.92f, 1f));
        shapeRenderer.rect(x + 10, y + 48, 12, 3);
        shapeRenderer.rect(x + 12, y + 43, 3, 8);
        shapeRenderer.rect(x + 42, y + 13, 11, 3);
        shapeRenderer.rect(x + 50, y + 13, 3, 9);
        shapeRenderer.rect(x + 14, y + 12, 4, 10);
        shapeRenderer.rect(x + 18, y + 18, 9, 3);
        shapeRenderer.rect(x + 39, y + 43, 4, 10);
        shapeRenderer.rect(x + 34, y + 50, 9, 3);
    }

    private void renderPortalGlow(ShapeRenderer shapeRenderer, float x, float y) {
        shapeRenderer.setColor(new Color(0.10f, 0.34f, 0.36f, 1f));
        shapeRenderer.rect(x + 17, y + 28, 30, 6);
        shapeRenderer.rect(x + 24, y + 20, 16, 22);
        shapeRenderer.setColor(new Color(0.62f, 1f, 0.88f, 1f));
        shapeRenderer.rect(x + 23, y + 31, 18, 2);
        shapeRenderer.rect(x + 30, y + 24, 4, 16);
        shapeRenderer.setColor(new Color(0.16f, 0.66f, 0.64f, 1f));
        shapeRenderer.rect(x + 28, y + 27, 8, 8);
    }

    private Color getFloorColor(int row, int col) {
        float checker = ((row + col) % 2 == 0) ? 0.010f : 0f;

        if (levelNumber == 1) {
            return new Color(0.045f + checker, 0.060f + checker, 0.064f + checker, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.050f + checker, 0.043f + checker, 0.070f + checker, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.086f + checker, 0.062f + checker, 0.040f + checker, 1f);
        }

        return new Color(0.035f + checker, 0.032f + checker, 0.055f + checker, 1f);
    }

    private void renderWallDetail(ShapeRenderer shapeRenderer, int col, int row) {
        float x = col * TILE_SIZE;
        float y = row * TILE_SIZE;

        shapeRenderer.setColor(getWallShadowColor());
        shapeRenderer.rect(x, y, TILE_SIZE, 4);
        shapeRenderer.rect(x, y, 4, TILE_SIZE);
        shapeRenderer.setColor(getWallHighlightColor());
        shapeRenderer.rect(x + 5, y + TILE_SIZE - 6, TILE_SIZE - 10, 2);
        shapeRenderer.rect(x + TILE_SIZE - 6, y + 6, 2, TILE_SIZE - 12);

        if (levelNumber == 1) {
            renderColdStoneWall(shapeRenderer, x, y, col, row);
        } else if (levelNumber == 2) {
            renderDeepDungeonWall(shapeRenderer, x, y, col, row);
        } else if (levelNumber == 3) {
            renderRuinWall(shapeRenderer, x, y, col, row);
        } else {
            renderFinalMazeWall(shapeRenderer, x, y, col, row);
        }
    }

    private void renderColdStoneWall(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        shapeRenderer.setColor(new Color(0.115f, 0.145f, 0.150f, 1f));
        shapeRenderer.rect(x + 6, y + 12, 52, 4);
        shapeRenderer.rect(x + 6, y + 38, 52, 4);
        shapeRenderer.rect(x + 25, y + 16, 4, 22);
        shapeRenderer.rect(x + 43, y + 42, 4, 15);

        shapeRenderer.setColor(new Color(0.27f, 0.34f, 0.34f, 1f));
        shapeRenderer.rect(x + 9, y + 46, 22, 2);
        shapeRenderer.rect(x + 34, y + 21, 17, 2);
        shapeRenderer.rect(x + 12, y + 18, 8, 2);

        shapeRenderer.setColor(new Color(0.09f, 0.17f, 0.14f, 1f));
        shapeRenderer.rect(x + 8, y + 6, 9, 3);
        shapeRenderer.rect(x + 17, y + 6, 3, 8);
        shapeRenderer.rect(x + 47, y + 31, 7, 3);

        if ((row * 7 + col * 3) % 5 == 0) {
            shapeRenderer.setColor(new Color(0.075f, 0.095f, 0.100f, 1f));
            shapeRenderer.rect(x + 16, y + 27, 24, 3);
            shapeRenderer.rect(x + 19, y + 23, 3, 5);
        }
    }

    private void renderDeepDungeonWall(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        shapeRenderer.setColor(new Color(0.105f, 0.080f, 0.145f, 1f));
        shapeRenderer.rect(x + 7, y + 9, 22, 18);
        shapeRenderer.rect(x + 35, y + 32, 22, 20);
        shapeRenderer.rect(x + 11, y + 39, 18, 13);

        shapeRenderer.setColor(new Color(0.30f, 0.23f, 0.38f, 1f));
        shapeRenderer.rect(x + 11, y + 25, 14, 2);
        shapeRenderer.rect(x + 39, y + 48, 14, 2);
        shapeRenderer.rect(x + 14, y + 50, 10, 2);

        if ((row + col) % 3 == 0) {
            shapeRenderer.setColor(new Color(0.12f, 0.42f, 0.48f, 1f));
            shapeRenderer.triangle(x + 29, y + 14, x + 37, y + 14, x + 33, y + 25);
            shapeRenderer.setColor(new Color(0.42f, 0.92f, 0.95f, 1f));
            shapeRenderer.rect(x + 32, y + 17, 2, 7);
        }

        if ((row * 3 + col) % 7 == 0) {
            shapeRenderer.setColor(new Color(0.34f, 0.14f, 0.28f, 1f));
            shapeRenderer.rect(x + 47, y + 10, 6, 18);
            shapeRenderer.rect(x + 45, y + 25, 10, 3);
        }
    }

    private void renderRuinWall(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        shapeRenderer.setColor(new Color(0.19f, 0.12f, 0.065f, 1f));
        shapeRenderer.rect(x + 6, y + 18, 52, 5);
        shapeRenderer.rect(x + 17, y + 24, 5, 21);
        shapeRenderer.rect(x + 40, y + 9, 5, 17);
        shapeRenderer.rect(x + 28, y + 40, 4, 16);

        shapeRenderer.setColor(new Color(0.45f, 0.32f, 0.18f, 1f));
        shapeRenderer.rect(x + 10, y + 49, 18, 2);
        shapeRenderer.rect(x + 32, y + 14, 17, 2);
        shapeRenderer.rect(x + 47, y + 31, 8, 2);

        if ((row * 5 + col) % 4 == 0) {
            shapeRenderer.setColor(new Color(0.37f, 0.17f, 0.07f, 1f));
            shapeRenderer.rect(x + 13, y + 35, 7, 5);
            shapeRenderer.rect(x + 22, y + 38, 11, 3);
        }

        if ((row + col * 2) % 6 == 0) {
            shapeRenderer.setColor(new Color(0.12f, 0.08f, 0.045f, 1f));
            shapeRenderer.rect(x + 34, y + 25, 3, 13);
            shapeRenderer.rect(x + 37, y + 25, 7, 3);
        }
    }

    private void renderFinalMazeWall(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        shapeRenderer.setColor(new Color(0.075f, 0.060f, 0.120f, 1f));
        shapeRenderer.rect(x + 7, y + 7, 20, 20);
        shapeRenderer.rect(x + 37, y + 37, 20, 20);
        shapeRenderer.rect(x + 9, y + 42, 20, 12);

        shapeRenderer.setColor(new Color(0.155f, 0.125f, 0.220f, 1f));
        shapeRenderer.rect(x + 11, y + 27, 42, 3);
        shapeRenderer.rect(x + 31, y + 10, 3, 44);
        shapeRenderer.rect(x + 40, y + 10, 12, 2);

        if ((row * 11 + col * 5 + levelNumber) % 4 == 0) {
            shapeRenderer.setColor(new Color(0.08f, 0.46f, 0.50f, 1f));
            shapeRenderer.rect(x + 14, y + 14, 5, 5);
            shapeRenderer.rect(x + 19, y + 16, 18, 2);
            shapeRenderer.rect(x + 37, y + 16, 2, 15);
            shapeRenderer.rect(x + 32, y + 29, 7, 2);
        }

        if ((row * 11 + col * 5 + levelNumber) % 9 == 0) {
            shapeRenderer.setColor(new Color(0.48f, 0.90f, 0.82f, 1f));
            shapeRenderer.rect(x + 43, y + 42, 8, 3);
            shapeRenderer.rect(x + 46, y + 38, 2, 10);
        }
    }

    private Color getWallShadowColor() {
        if (levelNumber == 1) {
            return new Color(0.08f, 0.10f, 0.11f, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.08f, 0.07f, 0.10f, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.11f, 0.08f, 0.05f, 1f);
        }

        return new Color(0.07f, 0.06f, 0.09f, 1f);
    }

    private Color getWallHighlightColor() {
        if (levelNumber == 1) {
            return new Color(0.31f, 0.37f, 0.38f, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.34f, 0.30f, 0.40f, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.43f, 0.34f, 0.23f, 1f);
        }

        return new Color(0.30f, 0.25f, 0.38f, 1f);
    }

    private void renderFloorDetail(ShapeRenderer shapeRenderer, int col, int row) {
        float x = col * TILE_SIZE;
        float y = row * TILE_SIZE;

        shapeRenderer.setColor(getFloorGrooveColor());
        shapeRenderer.rect(x, y, TILE_SIZE, 2);
        shapeRenderer.rect(x, y, 2, TILE_SIZE);

        shapeRenderer.setColor(getFloorEdgeLightColor());
        shapeRenderer.rect(x + 4, y + TILE_SIZE - 5, TILE_SIZE - 8, 1);
        shapeRenderer.rect(x + TILE_SIZE - 5, y + 4, 1, TILE_SIZE - 8);

        if (levelNumber == 1) {
            renderColdFloorDetail(shapeRenderer, x, y, col, row);
        } else if (levelNumber == 2) {
            renderDungeonFloorDetail(shapeRenderer, x, y, col, row);
        } else if (levelNumber == 3) {
            renderRuinFloorDetail(shapeRenderer, x, y, col, row);
        } else {
            renderFinalFloorDetail(shapeRenderer, x, y, col, row);
        }
    }

    private Color getFloorGrooveColor() {
        if (levelNumber == 1) {
            return new Color(0.025f, 0.034f, 0.038f, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.030f, 0.024f, 0.048f, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.050f, 0.034f, 0.020f, 1f);
        }

        return new Color(0.020f, 0.018f, 0.038f, 1f);
    }

    private Color getFloorEdgeLightColor() {
        if (levelNumber == 1) {
            return new Color(0.070f, 0.090f, 0.095f, 1f);
        }

        if (levelNumber == 2) {
            return new Color(0.070f, 0.055f, 0.105f, 1f);
        }

        if (levelNumber == 3) {
            return new Color(0.120f, 0.082f, 0.050f, 1f);
        }

        return new Color(0.060f, 0.050f, 0.100f, 1f);
    }

    private void renderColdFloorDetail(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        if ((row + col) % 4 == 0) {
            shapeRenderer.setColor(new Color(0.070f, 0.085f, 0.088f, 1f));
            shapeRenderer.rect(x + 18, y + 18, 10, 2);
            shapeRenderer.rect(x + 38, y + 42, 8, 2);
        }

        if ((row * 13 + col) % 10 == 0) {
            shapeRenderer.setColor(new Color(0.055f, 0.115f, 0.105f, 1f));
            shapeRenderer.rect(x + 9, y + 9, 15, 3);
            shapeRenderer.rect(x + 20, y + 12, 4, 8);
        }
    }

    private void renderDungeonFloorDetail(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        if ((row + col) % 3 == 0) {
            shapeRenderer.setColor(new Color(0.085f, 0.065f, 0.115f, 1f));
            shapeRenderer.rect(x + 14, y + 16, 16, 3);
            shapeRenderer.rect(x + 36, y + 39, 12, 3);
        }

        if ((row * 5 + col * 2) % 9 == 0) {
            shapeRenderer.setColor(new Color(0.13f, 0.31f, 0.34f, 1f));
            shapeRenderer.triangle(x + 44, y + 11, x + 52, y + 11, x + 48, y + 22);
            shapeRenderer.setColor(new Color(0.42f, 0.86f, 0.90f, 1f));
            shapeRenderer.rect(x + 47, y + 14, 2, 6);
        }
    }

    private void renderRuinFloorDetail(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        if ((row + col) % 4 == 0) {
            shapeRenderer.setColor(new Color(0.135f, 0.085f, 0.045f, 1f));
            shapeRenderer.rect(x + 15, y + 19, 13, 3);
            shapeRenderer.rect(x + 40, y + 37, 9, 3);
        }

        if ((row * 7 + col) % 8 == 0) {
            shapeRenderer.setColor(new Color(0.18f, 0.13f, 0.06f, 1f));
            shapeRenderer.rect(x + 7, y + 8, 9, 5);
            shapeRenderer.rect(x + 19, y + 9, 6, 3);
            shapeRenderer.setColor(new Color(0.060f, 0.040f, 0.022f, 1f));
            shapeRenderer.rect(x + 32, y + 23, 3, 18);
            shapeRenderer.rect(x + 35, y + 23, 8, 3);
        }
    }

    private void renderFinalFloorDetail(ShapeRenderer shapeRenderer, float x, float y, int col, int row) {
        if ((row + col) % 3 == 0) {
            shapeRenderer.setColor(new Color(0.055f, 0.045f, 0.092f, 1f));
            shapeRenderer.rect(x + 12, y + 16, 18, 3);
            shapeRenderer.rect(x + 35, y + 40, 13, 3);
        }

        if ((row * 11 + col * 3) % 8 == 0) {
            shapeRenderer.setColor(new Color(0.055f, 0.34f, 0.38f, 1f));
            shapeRenderer.rect(x + 20, y + 20, 22, 2);
            shapeRenderer.rect(x + 31, y + 13, 2, 16);
            shapeRenderer.setColor(new Color(0.40f, 0.85f, 0.78f, 1f));
            shapeRenderer.rect(x + 30, y + 19, 4, 4);
        }
    }

    private void renderFloorDecoration(ShapeRenderer shapeRenderer, int col, int row) {
        float x = col * TILE_SIZE;
        float y = row * TILE_SIZE;
        int seed = row * 31 + col * 17 + levelNumber * 13;

        if (seed % 11 == 0) {
            shapeRenderer.setColor(levelNumber == 3
                    ? new Color(0.16f, 0.11f, 0.055f, 1f)
                    : new Color(0.080f, 0.095f, 0.095f, 1f));
            shapeRenderer.rect(x + 12, y + 13, 16, 2);
            shapeRenderer.rect(x + 27, y + 13, 2, 10);
            shapeRenderer.rect(x + 29, y + 22, 9, 2);
        } else if (seed % 13 == 0) {
            shapeRenderer.setColor(levelNumber == 2
                    ? new Color(0.085f, 0.050f, 0.110f, 1f)
                    : new Color(0.060f, 0.075f, 0.070f, 1f));
            shapeRenderer.rect(x + 13, y + 11, 8, 5);
            shapeRenderer.rect(x + 25, y + 10, 10, 4);
            shapeRenderer.rect(x + 40, y + 13, 6, 3);
        } else if (seed % 17 == 0) {
            shapeRenderer.setColor(levelNumber == 4
                    ? new Color(0.080f, 0.052f, 0.125f, 1f)
                    : new Color(0.12f, 0.095f, 0.060f, 1f));
            shapeRenderer.rect(x + 44, y + 9, 7, 12);
            shapeRenderer.setColor(levelNumber == 4
                    ? new Color(0.13f, 0.10f, 0.18f, 1f)
                    : new Color(0.18f, 0.13f, 0.070f, 1f));
            shapeRenderer.rect(x + 42, y + 20, 11, 3);
        }
    }

    public void update(float delta, Player player) {
        damageCooldown = Math.max(0f, damageCooldown - delta);

        for (Enemy enemy : enemies) {
            enemy.update(player, this, delta);

            if (enemy.overlaps(player)) {
                if (enemy.canBeStealthKilledBy(player, this)) {
                    statusMessage = player.isActiveItem(Knife.TYPE)
                            ? "Enemy is unaware. Press F now."
                            : "Enemy is unaware. Select knife (3), then press F.";
                    continue;
                }

                if (damageCooldown > 0f || !enemy.canCatch(player, this)) {
                    continue;
                }

                player.takeDamage(1);
                player.moveTo(spawnX, spawnY);
                resetEnemiesToPatrol();
                damageCooldown = 3f;
                statusMessage = player.isDead() ? "Game Over." : "Caught by enemy. HP -1.";
                return;
            }
        }
    }

    private void resetEnemiesToPatrol() {
        for (Enemy enemy : enemies) {
            enemy.resetToPatrol();
        }
    }

    public boolean isBlocked(float x, float y, int width, int height) {
        Rectangle nextBounds = new Rectangle(x, y, width, height);

        if (isWall(x, y, width, height)) {
            return true;
        }

        for (DoorEntity door : doors) {
            if (!door.isOpen() && door.getBounds().overlaps(nextBounds)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasLineOfSight(float startX, float startY, float endX, float endY) {
        float dx = endX - startX;
        float dy = endY - startY;
        int steps = Math.max(1, (int) (Math.max(Math.abs(dx), Math.abs(dy)) / (TILE_SIZE / 4f)));

        for (int i = 1; i <= steps; i++) {
            float t = i / (float) steps;
            float checkX = startX + dx * t;
            float checkY = startY + dy * t;

            if (isWallPoint(checkX, checkY)) {
                return false;
            }
        }

        return true;
    }

    public Vector2 findNextStep(float startX, float startY, float targetX, float targetY) {
        int startTileX = worldToTile(startX + 16);
        int startTileY = worldToTile(startY + 16);
        int targetTileX = worldToTile(targetX + 16);
        int targetTileY = worldToTile(targetY + 16);

        if (!isInsideMap(startTileX, startTileY) || !isInsideMap(targetTileX, targetTileY)) {
            return new Vector2(targetX, targetY);
        }

        if (startTileX == targetTileX && startTileY == targetTileY) {
            return new Vector2(targetX, targetY);
        }

        int height = map.length;
        int width = map[0].length;
        boolean[][] visited = new boolean[height][width];
        int[][] previousX = new int[height][width];
        int[][] previousY = new int[height][width];

        for (int row = 0; row < height; row++) {
            Arrays.fill(previousX[row], -1);
            Arrays.fill(previousY[row], -1);
        }

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startTileX, startTileY});
        visited[startTileY][startTileX] = true;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            int[] current = queue.remove();

            if (current[0] == targetTileX && current[1] == targetTileY) {
                break;
            }

            for (int i = 0; i < dx.length; i++) {
                int nextTileX = current[0] + dx[i];
                int nextTileY = current[1] + dy[i];

                if (!isInsideMap(nextTileX, nextTileY) ||
                        visited[nextTileY][nextTileX] ||
                        isTileBlockedForPath(nextTileX, nextTileY)) {
                    continue;
                }

                visited[nextTileY][nextTileX] = true;
                previousX[nextTileY][nextTileX] = current[0];
                previousY[nextTileY][nextTileX] = current[1];
                queue.add(new int[]{nextTileX, nextTileY});
            }
        }

        if (!visited[targetTileY][targetTileX]) {
            return null;
        }

        int stepX = targetTileX;
        int stepY = targetTileY;

        while (previousX[stepY][stepX] != startTileX || previousY[stepY][stepX] != startTileY) {
            int parentX = previousX[stepY][stepX];
            int parentY = previousY[stepY][stepX];

            if (parentX == -1 || parentY == -1) {
                return null;
            }

            stepX = parentX;
            stepY = parentY;
        }

        return new Vector2(tileX(stepX) + 16, tileY(stepY) + 16);
    }

    public boolean canReach(float startX, float startY, float targetX, float targetY) {
        return findNextStep(startX, startY, targetX, targetY) != null;
    }

    public float getTileCenterX(float worldX) {
        return tileX(worldToTile(worldX)) + 16;
    }

    public float getTileCenterY(float worldY) {
        return tileY(worldToTile(worldY)) + 16;
    }

    public boolean pickUpNearbyItem(Player player) {
        Rectangle interactionBounds = getInteractionBounds(player);

        for (ItemEntity item : items) {
            if (!item.isPickedUp() && item.getBounds().overlaps(interactionBounds)) {
                item.getItem().onPickup(player);
                item.markPickedUp();
                statusMessage = "Picked up: " + item.getItem().getType();
                return true;
            }
        }

        return false;
    }

    public boolean openNearbyDoor(Player player) {
        Rectangle interactionBounds = getInteractionBounds(player);

        for (DoorEntity door : doors) {
            if (!door.isOpen() && door.getBounds().overlaps(interactionBounds)) {
                boolean opened = door.tryOpen(player);
                if (opened) {
                    statusMessage = "Door opened.";
                } else if (player.hasItem(door.getRequiredItem())) {
                    statusMessage = "Select " + door.getRequiredItem() + " first, then press E.";
                } else {
                    statusMessage = "Door locked. You need: " + door.getRequiredItem();
                }
                return opened;
            }
        }

        statusMessage = "Nothing to interact with.";
        return false;
    }

    public boolean stealthKillNearbyEnemy(Player player) {
        if (!player.hasItem(Knife.TYPE)) {
            statusMessage = "Need knife for stealth kill.";
            return false;
        }

        if (!player.isActiveItem(Knife.TYPE)) {
            statusMessage = "Select knife first, then press F.";
            return false;
        }

        Enemy closestEnemy = null;
        float closestDistance = Float.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (enemy.canBeStealthKilledBy(player, this)) {
                float distance = enemy.distanceTo(player);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEnemy = enemy;
                }
            }
        }

        if (closestEnemy != null) {
            closestEnemy.kill();
            damageCooldown = 0.75f;
            statusMessage = "Enemy eliminated.";
            return true;
        }

        statusMessage = "Knife only works from behind before the enemy reacts.";
        return false;
    }

    public void selectPlayerItem(Player player, String itemType) {
        if (player.selectItem(itemType)) {
            statusMessage = "Selected: " + itemType;
        } else {
            statusMessage = "You do not have: " + itemType;
        }
    }

    public boolean isExitReached(Player player) {
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float playerCenterY = player.getY() + player.getHeight() / 2f;
        boolean reached = exitBounds.contains(playerCenterX, playerCenterY);

        if (reached) {
            statusMessage = "Level complete.";
        }

        return reached;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public float getSpawnX() {
        return spawnX;
    }

    public float getSpawnY() {
        return spawnY;
    }

    public boolean isFinalLevel() {
        return levelNumber >= 4;
    }

    private int[][] createMap(int levelNumber) {
        if (levelNumber == 2) {
            return LEVEL_2;
        }

        if (levelNumber == 3) {
            return LEVEL_3;
        }

        if (levelNumber >= 4) {
            return LEVEL_4;
        }

        return LEVEL_1;
    }

    private Rectangle createExitBounds(int levelNumber) {
        if (levelNumber == 1 || levelNumber == 2 || levelNumber == 3) {
            return new Rectangle(tileX(16), tileY(13), TILE_SIZE, TILE_SIZE);
        }

        return new Rectangle(tileX(12), tileY(1), TILE_SIZE, TILE_SIZE);
    }

    private void createLevelObjects(int levelNumber) {
        if (levelNumber == 1) {
            addItem(Key.TYPE, 15, 1);
            addDoor(Key.TYPE, 15, 13);
            statusMessage = "Level 1: explore the maze, find the key, then return to the door.";
            return;
        }

        if (levelNumber == 2) {
            addItem(Key.TYPE, 15, 1);
            addDoor(Key.TYPE, 15, 13);
            addEnemy(7, 5, new int[][]{{7,5}, {8,5}, {9,5}, {8,5}});
            addEnemy(13, 9, new int[][]{{13,9}, {13,10}, {13,11}, {13,10}});
            statusMessage = "Level 2: longer maze, same key-door logic.";
            return;
        }

        if (levelNumber == 3) {
            addItem(Crowbar.TYPE, 15, 1);
            addItem(Knife.TYPE, 1, 13);
            addDoor(Crowbar.TYPE, 15, 13);
            addEnemy(11, 5, new int[][]{{11,5}, {12,5}, {13,5}, {12,5}});
            addEnemy(11, 9, new int[][]{{11,9}, {12,9}, {13,9}, {12,9}});
            addEnemy(3, 11, new int[][]{{3,11}, {4,11}, {5,11}, {4,11}});
            statusMessage = "Level 3: find the crowbar for the iron door.";
            return;
        }

        addItem(Key.TYPE, 3, 1);
        addItem(Crowbar.TYPE, 11, 5);
        addItem(Knife.TYPE, 7, 3);
        addDoor(Key.TYPE, 12, 7);
        addDoor(Crowbar.TYPE, 11, 1);
        addEnemy(8, 3, new int[][]{{8,3}, {9,3}, {10,3}, {9,3}});
        addEnemy(9, 7, new int[][]{{9,7}, {8,7}, {7,7}, {8,7}});
        addEnemy(3, 9, new int[][]{{3,9}, {2,9}, {1,9}, {2,9}});
        addEnemy(7, 11, new int[][]{{7,11}, {8,11}, {9,11}, {8,11}});
        statusMessage = "Level 4: use both items to reach the final exit.";
    }

    private void addItem(String itemType, int tileX, int tileY) {
        items.add(new ItemEntity(ItemFactory.create(itemType), tileX(tileX), tileY(tileY)));
    }

    private void addDoor(String requiredItem, int tileX, int tileY) {
        Color color = Key.TYPE.equals(requiredItem) ? WOODEN_DOOR_COLOR : IRON_DOOR_COLOR;

        doors.add(new DoorEntity(
                new LockedDoor(new BaseDoor(), requiredItem),
                tileX(tileX),
                tileY(tileY),
                color,
                requiredItem
        ));
    }

    private void addEnemy(int tileX, int tileY, int[][] patrolTiles) {
        List<Vector2> patrolPath = new ArrayList<>();

        for (int[] tile : patrolTiles) {
            patrolPath.add(new Vector2(tileX(tile[0]) + 16, tileY(tile[1]) + 16));
        }

        enemies.add(new Enemy(tileX(tileX) + 16, tileY(tileY) + 16, patrolPath));
    }

    private float tileX(int tileX) {
        return tileX * TILE_SIZE;
    }

    private float tileY(int tileY) {
        return tileY * TILE_SIZE;
    }

    private int worldToTile(float value) {
        return (int) (value / TILE_SIZE);
    }

    private boolean isInsideMap(int tileX, int tileY) {
        return tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length;
    }

    private boolean isTileBlockedForPath(int tileX, int tileY) {
        if (map[tileY][tileX] == WALL) {
            return true;
        }

        Rectangle tileBounds = new Rectangle(tileX(tileX), tileY(tileY), TILE_SIZE, TILE_SIZE);

        for (DoorEntity door : doors) {
            if (!door.isOpen() && door.getBounds().overlaps(tileBounds)) {
                return true;
            }
        }

        return false;
    }

    private Rectangle getInteractionBounds(Player player) {
        return new Rectangle(
                player.getX() - 16,
                player.getY() - 16,
                player.getWidth() + 32,
                player.getHeight() + 32
        );
    }

    private boolean isWall(float x, float y, int width, int height) {
        int leftTile = (int)(x / TILE_SIZE);
        int rightTile = (int)((x + width - 1) / TILE_SIZE);

        int bottomTile = (int)(y / TILE_SIZE);
        int topTile = (int)((y + height - 1) / TILE_SIZE);

        if (leftTile < 0 || rightTile >= map[0].length || bottomTile < 0 || topTile >= map.length) {
            return true;
        }

        return map[bottomTile][leftTile] == WALL ||
                map[bottomTile][rightTile] == WALL ||
                map[topTile][leftTile] == WALL ||
                map[topTile][rightTile] == WALL;
    }

    private boolean isWallPoint(float x, float y) {
        int tileX = (int) (x / TILE_SIZE);
        int tileY = (int) (y / TILE_SIZE);

        if (tileX < 0 || tileX >= map[0].length || tileY < 0 || tileY >= map.length) {
            return true;
        }

        return map[tileY][tileX] == WALL;
    }
}
