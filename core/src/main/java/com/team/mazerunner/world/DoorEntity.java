package com.team.mazerunner.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.items.Key;

public class DoorEntity {

    private final Door door;
    private final Rectangle bounds;
    private final Color color;
    private final String requiredItem;
    private boolean open;

    public DoorEntity(Door door, float x, float y, Color color, String requiredItem) {
        this.door = door;
        this.bounds = new Rectangle(x, y, LevelMap.TILE_SIZE, LevelMap.TILE_SIZE);
        this.color = color;
        this.requiredItem = requiredItem;
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (open) {
            return;
        }

        shapeRenderer.setColor(new Color(0.015f, 0.014f, 0.012f, 1f));
        shapeRenderer.rect(bounds.x + 5, bounds.y - 4, bounds.width - 10, 5);

        if (Key.TYPE.equals(requiredItem)) {
            renderWoodenDoor(shapeRenderer);
        } else {
            renderIronDoor(shapeRenderer);
        }
    }

    private void renderWoodenDoor(ShapeRenderer shapeRenderer) {
        float x = bounds.x;
        float y = bounds.y;

        shapeRenderer.setColor(new Color(0.055f, 0.035f, 0.020f, 1f));
        shapeRenderer.rect(x - 4, y - 2, bounds.width + 8, bounds.height + 4);

        shapeRenderer.setColor(new Color(0.24f, 0.12f, 0.045f, 1f));
        shapeRenderer.rect(x, y, bounds.width, bounds.height);
        shapeRenderer.setColor(new Color(0.45f, 0.23f, 0.08f, 1f));
        shapeRenderer.rect(x + 6, y + 6, bounds.width - 12, bounds.height - 12);

        shapeRenderer.setColor(new Color(0.30f, 0.15f, 0.05f, 1f));
        shapeRenderer.rect(x + 12, y + 8, 4, bounds.height - 16);
        shapeRenderer.rect(x + 29, y + 8, 4, bounds.height - 16);
        shapeRenderer.rect(x + 46, y + 8, 4, bounds.height - 16);
        shapeRenderer.rect(x + 8, y + 20, bounds.width - 16, 4);
        shapeRenderer.rect(x + 8, y + 42, bounds.width - 16, 4);

        shapeRenderer.setColor(new Color(0.62f, 0.36f, 0.14f, 1f));
        shapeRenderer.rect(x + 14, y + 10, 2, bounds.height - 22);
        shapeRenderer.rect(x + 31, y + 10, 2, bounds.height - 22);
        shapeRenderer.rect(x + 48, y + 10, 2, bounds.height - 22);
        shapeRenderer.rect(x + 12, y + 45, 18, 2);

        renderKeyLock(shapeRenderer, x + 38, y + 28);
    }

    private void renderIronDoor(ShapeRenderer shapeRenderer) {
        float x = bounds.x;
        float y = bounds.y;

        shapeRenderer.setColor(new Color(0.030f, 0.035f, 0.040f, 1f));
        shapeRenderer.rect(x - 4, y - 2, bounds.width + 8, bounds.height + 4);

        shapeRenderer.setColor(new Color(0.18f, 0.22f, 0.25f, 1f));
        shapeRenderer.rect(x, y, bounds.width, bounds.height);
        shapeRenderer.setColor(new Color(0.36f, 0.42f, 0.46f, 1f));
        shapeRenderer.rect(x + 6, y + 6, bounds.width - 12, bounds.height - 12);

        shapeRenderer.setColor(new Color(0.16f, 0.20f, 0.23f, 1f));
        shapeRenderer.rect(x + 10, y + 10, bounds.width - 20, 8);
        shapeRenderer.rect(x + 10, y + 28, bounds.width - 20, 8);
        shapeRenderer.rect(x + 10, y + 46, bounds.width - 20, 8);
        shapeRenderer.rect(x + 18, y + 8, 5, bounds.height - 16);
        shapeRenderer.rect(x + 41, y + 8, 5, bounds.height - 16);

        shapeRenderer.setColor(new Color(0.62f, 0.70f, 0.73f, 1f));
        shapeRenderer.rect(x + 12, y + 51, 14, 2);
        shapeRenderer.rect(x + 36, y + 15, 14, 2);

        renderRivets(shapeRenderer);
        renderCrowbarLock(shapeRenderer, x + 34, y + 26);
    }

    private void renderKeyLock(ShapeRenderer shapeRenderer, float lockX, float lockY) {
        shapeRenderer.setColor(new Color(0.08f, 0.055f, 0.025f, 1f));
        shapeRenderer.rect(lockX - 3, lockY - 4, 15, 14);
        shapeRenderer.setColor(new Color(0.86f, 0.62f, 0.20f, 1f));
        shapeRenderer.rect(lockX, lockY, 9, 8);
        shapeRenderer.setColor(new Color(0.15f, 0.10f, 0.035f, 1f));
        shapeRenderer.rect(lockX + 4, lockY + 2, 2, 4);
        shapeRenderer.setColor(new Color(1f, 0.82f, 0.38f, 1f));
        shapeRenderer.rect(lockX + 2, lockY + 6, 5, 1);
    }

    private void renderCrowbarLock(ShapeRenderer shapeRenderer, float lockX, float lockY) {
        shapeRenderer.setColor(new Color(0.06f, 0.07f, 0.08f, 1f));
        shapeRenderer.rect(lockX - 3, lockY - 4, 17, 16);
        shapeRenderer.setColor(new Color(0.64f, 0.70f, 0.73f, 1f));
        shapeRenderer.rect(lockX, lockY, 11, 10);
        shapeRenderer.setColor(new Color(0.18f, 0.22f, 0.25f, 1f));
        shapeRenderer.rect(lockX + 3, lockY + 3, 5, 4);
        shapeRenderer.setColor(new Color(0.85f, 0.94f, 0.95f, 1f));
        shapeRenderer.rect(lockX + 2, lockY + 8, 7, 1);
    }

    private void renderRivets(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.70f, 0.76f, 0.78f, 1f));

        for (int row = 0; row < 3; row++) {
            float rivetY = bounds.y + 14 + row * 18;
            shapeRenderer.rect(bounds.x + 12, rivetY, 4, 4);
            shapeRenderer.rect(bounds.x + bounds.width - 16, rivetY, 4, 4);
        }
    }

    public boolean tryOpen(Player player) {
        if (open) {
            return true;
        }

        open = door.open(player);
        return open;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isOpen() {
        return open;
    }

    public String getRequiredItem() {
        return requiredItem;
    }
}
