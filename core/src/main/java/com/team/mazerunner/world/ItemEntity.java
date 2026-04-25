package com.team.mazerunner.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Item;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;

public class ItemEntity {

    private final Item item;
    private final Rectangle bounds;
    private boolean pickedUp;

    public ItemEntity(Item item, float x, float y) {
        this.item = item;
        this.bounds = new Rectangle(x + 16, y + 16, 32, 32);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (pickedUp) {
            return;
        }

        shapeRenderer.setColor(new Color(0.018f, 0.022f, 0.026f, 1f));
        shapeRenderer.rect(bounds.x + 4, bounds.y - 3, bounds.width - 8, 4);

        if (Key.TYPE.equals(item.getType())) {
            renderKey(shapeRenderer);
        } else if (Crowbar.TYPE.equals(item.getType())) {
            renderCrowbar(shapeRenderer);
        } else if (Knife.TYPE.equals(item.getType())) {
            renderKnife(shapeRenderer);
        }
    }

    private void renderKey(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.16f, 0.11f, 0.03f, 1f));
        shapeRenderer.circle(bounds.x + 11, bounds.y + 20, 9);
        shapeRenderer.rect(bounds.x + 17, bounds.y + 17, 14, 7);

        shapeRenderer.setColor(new Color(0.92f, 0.63f, 0.12f, 1f));
        shapeRenderer.circle(bounds.x + 11, bounds.y + 20, 8);
        shapeRenderer.setColor(new Color(0.07f, 0.08f, 0.08f, 1f));
        shapeRenderer.circle(bounds.x + 11, bounds.y + 20, 4);
        shapeRenderer.setColor(new Color(0.92f, 0.63f, 0.12f, 1f));
        shapeRenderer.rect(bounds.x + 17, bounds.y + 18, 13, 5);
        shapeRenderer.rect(bounds.x + 25, bounds.y + 12, 4, 6);
        shapeRenderer.rect(bounds.x + 20, bounds.y + 12, 4, 6);
        shapeRenderer.setColor(new Color(1f, 0.86f, 0.35f, 1f));
        shapeRenderer.rect(bounds.x + 18, bounds.y + 22, 10, 2);
        shapeRenderer.rect(bounds.x + 8, bounds.y + 25, 4, 2);
    }

    private void renderCrowbar(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.08f, 0.07f, 0.07f, 1f));
        shapeRenderer.rect(bounds.x + 8, bounds.y + 6, 8, 23);
        shapeRenderer.rect(bounds.x + 14, bounds.y + 24, 13, 8);
        shapeRenderer.rect(bounds.x + 22, bounds.y + 19, 6, 7);

        shapeRenderer.setColor(new Color(0.45f, 0.18f, 0.12f, 1f));
        shapeRenderer.rect(bounds.x + 10, bounds.y + 7, 8, 22);
        shapeRenderer.rect(bounds.x + 15, bounds.y + 25, 11, 5);
        shapeRenderer.rect(bounds.x + 22, bounds.y + 20, 4, 6);

        shapeRenderer.setColor(new Color(0.86f, 0.37f, 0.20f, 1f));
        shapeRenderer.rect(bounds.x + 12, bounds.y + 9, 3, 18);
        shapeRenderer.rect(bounds.x + 17, bounds.y + 27, 7, 2);

        shapeRenderer.setColor(new Color(0.18f, 0.08f, 0.06f, 1f));
        shapeRenderer.rect(bounds.x + 9, bounds.y + 6, 3, 4);
        shapeRenderer.rect(bounds.x + 23, bounds.y + 19, 5, 3);
    }

    private void renderKnife(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.035f, 0.040f, 0.045f, 1f));
        shapeRenderer.rect(bounds.x + 13, bounds.y + 5, 7, 11);
        shapeRenderer.rect(bounds.x + 9, bounds.y + 14, 15, 5);
        shapeRenderer.triangle(
                bounds.x + 10, bounds.y + 17,
                bounds.x + 24, bounds.y + 17,
                bounds.x + 18, bounds.y + 31
        );

        shapeRenderer.setColor(new Color(0.40f, 0.08f, 0.10f, 1f));
        shapeRenderer.rect(bounds.x + 14, bounds.y + 6, 5, 9);
        shapeRenderer.setColor(new Color(0.78f, 0.50f, 0.24f, 1f));
        shapeRenderer.rect(bounds.x + 13, bounds.y + 7, 7, 2);
        shapeRenderer.rect(bounds.x + 12, bounds.y + 13, 10, 4);

        shapeRenderer.setColor(new Color(0.60f, 0.66f, 0.70f, 1f));
        shapeRenderer.triangle(
                bounds.x + 11, bounds.y + 18,
                bounds.x + 23, bounds.y + 18,
                bounds.x + 18, bounds.y + 30
        );
        shapeRenderer.setColor(new Color(0.92f, 0.96f, 0.98f, 1f));
        shapeRenderer.triangle(
                bounds.x + 16, bounds.y + 19,
                bounds.x + 22, bounds.y + 19,
                bounds.x + 18, bounds.y + 29
        );
        shapeRenderer.setColor(new Color(0.28f, 0.32f, 0.35f, 1f));
        shapeRenderer.rect(bounds.x + 16, bounds.y + 18, 2, 11);
    }

    public Item getItem() {
        return item;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void markPickedUp() {
        pickedUp = true;
    }
}
