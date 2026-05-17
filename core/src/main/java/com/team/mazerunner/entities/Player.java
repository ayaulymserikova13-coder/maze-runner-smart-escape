package com.team.mazerunner.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Player {

    public static final int MAX_HP = 3;

    private static final float DISGUISE_DURATION = 5f;
    private static final float HEAL_EFFECT_DURATION = 1.1f;

    private float x;
    private float y;
    private float facingX = 1f;
    private float facingY = 0f;
    private float disguiseTimer;
    private float healEffectTimer;

    private final float speed = 250f;
    private int hp = MAX_HP;
    private final Set<String> inventory = new LinkedHashSet<>();
    private final List<HPObserver> hpObservers = new ArrayList<>();
    private String activeItem;

    private final int width = 32;
    private final int height = 32;

    private final Rectangle bounds;

    public Player(float x, float y) {

        this.x = x;
        this.y = y;

        bounds = new Rectangle(x, y, width, height);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (isDisguised()) {
            renderDisguiseEffect(shapeRenderer);
            renderHealEffect(shapeRenderer);
            return;
        }

        renderShadow(shapeRenderer);
        renderCloak(shapeRenderer);
        renderLegs(shapeRenderer);
        renderHead(shapeRenderer);
        renderArms(shapeRenderer);
        renderHeldItem(shapeRenderer);
        renderHealEffect(shapeRenderer);
    }

    public void update(float delta) {
        disguiseTimer = Math.max(0f, disguiseTimer - delta);
        healEffectTimer = Math.max(0f, healEffectTimer - delta);
    }

    private void renderDisguiseEffect(ShapeRenderer shapeRenderer) {
        float pulse = disguiseTimer - (int) disguiseTimer;
        float shimmerOffset = pulse < 0.5f ? 0f : 2f;

        shapeRenderer.setColor(new Color(0.010f, 0.015f, 0.018f, 1f));
        shapeRenderer.rect(x + 7, y - 3, width - 14, 3);

        shapeRenderer.setColor(new Color(0.035f, 0.080f, 0.085f, 1f));
        shapeRenderer.rect(x + 10, y + 5, 12, 19);
        shapeRenderer.circle(x + 16, y + 24, 6);

        shapeRenderer.setColor(new Color(0.11f, 0.28f, 0.27f, 1f));
        shapeRenderer.rect(x + 8 + shimmerOffset, y + 7, 2, 14);
        shapeRenderer.rect(x + 23 - shimmerOffset, y + 7, 2, 14);
        shapeRenderer.rect(x + 11, y + 27, 10, 2);
        shapeRenderer.rect(x + 12, y + 3, 8, 2);

        shapeRenderer.setColor(new Color(0.44f, 0.84f, 0.76f, 1f));
        shapeRenderer.rect(x + 6, y + 15 + shimmerOffset, 6, 1);
        shapeRenderer.rect(x + 20, y + 19 - shimmerOffset, 7, 1);
        shapeRenderer.rect(x + 13, y + 23, 2, 2);
        shapeRenderer.rect(x + 19, y + 11, 2, 2);

        shapeRenderer.setColor(new Color(0.19f, 0.42f, 0.40f, 1f));
        shapeRenderer.rect(x + 4, y + 9, 3, 1);
        shapeRenderer.rect(x + 25, y + 26, 3, 1);
        shapeRenderer.rect(x + 17, y + 31, 1, 3);
    }

    private void renderHealEffect(ShapeRenderer shapeRenderer) {
        if (healEffectTimer <= 0f) {
            return;
        }

        float progress = 1f - healEffectTimer / HEAL_EFFECT_DURATION;
        float lift = progress * 12f;
        float centerX = x + width / 2f;
        float baseY = y + height + 8f + lift;

        shapeRenderer.setColor(new Color(0.04f, 0.16f, 0.09f, 1f));
        shapeRenderer.rect(centerX - 3, baseY - 9, 6, 18);
        shapeRenderer.rect(centerX - 9, baseY - 3, 18, 6);

        shapeRenderer.setColor(new Color(0.32f, 0.90f, 0.48f, 1f));
        shapeRenderer.rect(centerX - 2, baseY - 8, 4, 16);
        shapeRenderer.rect(centerX - 8, baseY - 2, 16, 4);

        shapeRenderer.setColor(new Color(0.70f, 1f, 0.76f, 1f));
        shapeRenderer.rect(centerX - 1, baseY + 1, 2, 5);
        shapeRenderer.rect(centerX + 7, baseY + 5, 3, 3);
        shapeRenderer.rect(centerX - 11, baseY + 2, 3, 3);
        shapeRenderer.rect(centerX + 5, baseY - 10, 2, 2);
    }

    private void renderShadow(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.012f, 0.016f, 0.018f, 1f));
        shapeRenderer.rect(x + 3, y - 4, width - 6, 5);
        shapeRenderer.setColor(new Color(0.030f, 0.040f, 0.045f, 1f));
        shapeRenderer.rect(x + 7, y - 2, width - 14, 3);
    }

    private void renderCloak(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.020f, 0.032f, 0.038f, 1f));
        shapeRenderer.rect(x + 7, y + 3, 18, 21);
        shapeRenderer.circle(x + 16, y + 24, 9);

        shapeRenderer.setColor(new Color(0.070f, 0.145f, 0.150f, 1f));
        shapeRenderer.rect(x + 9, y + 5, 14, 18);
        shapeRenderer.circle(x + 16, y + 23, 7);

        shapeRenderer.setColor(new Color(0.135f, 0.350f, 0.350f, 1f));
        shapeRenderer.rect(x + 11, y + 8, 10, 13);
        shapeRenderer.setColor(new Color(0.48f, 0.76f, 0.68f, 1f));
        shapeRenderer.rect(x + 12, y + 17, 8, 2);
        shapeRenderer.rect(x + 14, y + 8, 4, 9);

        shapeRenderer.setColor(new Color(0.42f, 0.28f, 0.14f, 1f));
        shapeRenderer.rect(x + 9, y + 10, 14, 3);
        shapeRenderer.setColor(new Color(0.78f, 0.56f, 0.24f, 1f));
        shapeRenderer.rect(x + 15, y + 10, 3, 3);
    }

    private void renderLegs(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.035f, 0.060f, 0.065f, 1f));

        if (facingX > 0f) {
            shapeRenderer.rect(x + 9, y, 5, 7);
            shapeRenderer.rect(x + 20, y + 1, 5, 7);
        } else if (facingX < 0f) {
            shapeRenderer.rect(x + 7, y + 1, 5, 7);
            shapeRenderer.rect(x + 18, y, 5, 7);
        } else {
            shapeRenderer.rect(x + 8, y, 5, 7);
            shapeRenderer.rect(x + 19, y, 5, 7);
        }

        shapeRenderer.setColor(new Color(0.095f, 0.185f, 0.185f, 1f));
        shapeRenderer.rect(x + 8, y, 6, 2);
        shapeRenderer.rect(x + 18, y, 6, 2);
    }

    private void renderHead(ShapeRenderer shapeRenderer) {
        if (facingY > 0f) {
            renderBackHead(shapeRenderer);
            return;
        }

        if (facingY < 0f) {
            renderFrontHead(shapeRenderer);
            return;
        }

        renderSideHead(shapeRenderer);
    }

    private void renderBackHead(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.018f, 0.027f, 0.032f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 8);
        shapeRenderer.setColor(new Color(0.070f, 0.145f, 0.150f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 6);
        shapeRenderer.setColor(new Color(0.20f, 0.50f, 0.48f, 1f));
        shapeRenderer.rect(x + 11, y + 21, 10, 3);
    }

    private void renderFrontHead(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.018f, 0.027f, 0.032f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 8);
        shapeRenderer.setColor(new Color(0.73f, 0.57f, 0.42f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 6);
        shapeRenderer.setColor(new Color(0.055f, 0.040f, 0.032f, 1f));
        shapeRenderer.rect(x + 9, y + 28, 14, 4);
        shapeRenderer.rect(x + 10, y + 21, 12, 3);
        renderEyes(shapeRenderer, x + 13, x + 18);
    }

    private void renderSideHead(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.018f, 0.027f, 0.032f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 8);
        shapeRenderer.setColor(new Color(0.73f, 0.57f, 0.42f, 1f));
        shapeRenderer.circle(x + 16, y + 25, 6);
        shapeRenderer.setColor(new Color(0.055f, 0.040f, 0.032f, 1f));
        shapeRenderer.rect(x + 8, y + 28, 16, 4);

        if (facingX > 0f) {
            shapeRenderer.rect(x + 8, y + 23, 4, 6);
            renderEyes(shapeRenderer, x + 19, -1);
        } else {
            shapeRenderer.rect(x + 20, y + 23, 4, 6);
            renderEyes(shapeRenderer, x + 11, -1);
        }
    }

    private void renderArms(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.055f, 0.105f, 0.110f, 1f));

        if (facingX > 0f) {
            shapeRenderer.rect(x + 23, y + 10, 4, 10);
            shapeRenderer.rect(x + 6, y + 9, 4, 9);
        } else if (facingX < 0f) {
            shapeRenderer.rect(x + 5, y + 10, 4, 10);
            shapeRenderer.rect(x + 22, y + 9, 4, 9);
        } else if (facingY > 0f) {
            shapeRenderer.rect(x + 7, y + 11, 4, 9);
            shapeRenderer.rect(x + 21, y + 11, 4, 9);
        } else {
            shapeRenderer.rect(x + 6, y + 9, 4, 10);
            shapeRenderer.rect(x + 22, y + 9, 4, 10);
        }
    }

    private void renderHeldItem(ShapeRenderer shapeRenderer) {
        if (activeItem == null || facingY > 0f) {
            return;
        }

        float itemX;
        float itemY;

        if (facingX < 0f) {
            itemX = x + 1;
            itemY = y + 12;
        } else if (facingX > 0f) {
            itemX = x + 25;
            itemY = y + 12;
        } else {
            itemX = x + 23;
            itemY = y + 13;
        }

        if (Key.TYPE.equals(activeItem)) {
            renderHeldKey(shapeRenderer, itemX, itemY);
        } else if (Crowbar.TYPE.equals(activeItem)) {
            renderHeldCrowbar(shapeRenderer, itemX, itemY);
        } else if (Knife.TYPE.equals(activeItem)) {
            renderHeldKnife(shapeRenderer, itemX, itemY);
        }
    }

    private void renderHeldKey(ShapeRenderer shapeRenderer, float itemX, float itemY) {
        shapeRenderer.setColor(new Color(0.15f, 0.10f, 0.03f, 1f));
        shapeRenderer.circle(itemX + 2, itemY + 5, 4);
        shapeRenderer.rect(itemX + 4, itemY + 3, 9, 4);
        shapeRenderer.setColor(new Color(0.92f, 0.63f, 0.12f, 1f));
        shapeRenderer.circle(itemX + 2, itemY + 5, 3);
        shapeRenderer.rect(itemX + 4, itemY + 4, 8, 2);
        shapeRenderer.rect(itemX + 10, itemY + 1, 2, 4);
        shapeRenderer.setColor(new Color(1f, 0.84f, 0.34f, 1f));
        shapeRenderer.rect(itemX + 5, itemY + 7, 5, 1);
    }

    private void renderHeldCrowbar(ShapeRenderer shapeRenderer, float itemX, float itemY) {
        shapeRenderer.setColor(new Color(0.10f, 0.05f, 0.04f, 1f));
        shapeRenderer.rect(itemX + 1, itemY - 2, 6, 17);
        shapeRenderer.rect(itemX + 5, itemY + 11, 9, 4);
        shapeRenderer.setColor(new Color(0.58f, 0.30f, 0.16f, 1f));
        shapeRenderer.rect(itemX + 2, itemY - 1, 4, 15);
        shapeRenderer.rect(itemX + 5, itemY + 11, 7, 3);
        shapeRenderer.rect(itemX + 10, itemY + 8, 3, 4);
        shapeRenderer.setColor(new Color(0.88f, 0.48f, 0.22f, 1f));
        shapeRenderer.rect(itemX + 4, itemY + 1, 1, 10);
    }

    private void renderHeldKnife(ShapeRenderer shapeRenderer, float itemX, float itemY) {
        shapeRenderer.setColor(new Color(0.035f, 0.040f, 0.045f, 1f));
        shapeRenderer.rect(itemX + 2, itemY - 1, 5, 8);
        shapeRenderer.rect(itemX - 1, itemY + 6, 11, 4);
        shapeRenderer.triangle(itemX, itemY + 9, itemX + 11, itemY + 9, itemX + 7, itemY + 20);

        shapeRenderer.setColor(new Color(0.38f, 0.08f, 0.10f, 1f));
        shapeRenderer.rect(itemX + 3, itemY, 3, 7);
        shapeRenderer.setColor(new Color(0.78f, 0.50f, 0.24f, 1f));
        shapeRenderer.rect(itemX, itemY + 7, 9, 2);

        shapeRenderer.setColor(new Color(0.68f, 0.74f, 0.78f, 1f));
        shapeRenderer.triangle(itemX + 1, itemY + 10, itemX + 10, itemY + 10, itemX + 7, itemY + 19);
        shapeRenderer.setColor(new Color(0.96f, 0.98f, 1f, 1f));
        shapeRenderer.triangle(itemX + 5, itemY + 11, itemX + 9, itemY + 11, itemX + 7, itemY + 18);
    }

    private void renderEyes(ShapeRenderer shapeRenderer, float firstEyeX, float secondEyeX) {
        shapeRenderer.setColor(new Color(0.025f, 0.035f, 0.04f, 1f));
        shapeRenderer.rect(firstEyeX, y + 25, 3, 3);
        if (secondEyeX >= 0f) {
            shapeRenderer.rect(secondEyeX, y + 25, 3, 3);
        }

        shapeRenderer.setColor(new Color(0.78f, 0.92f, 0.95f, 1f));
        shapeRenderer.rect(firstEyeX + 1, y + 26, 1, 1);
        if (secondEyeX >= 0f) {
            shapeRenderer.rect(secondEyeX + 1, y + 26, 1, 1);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHp() {
        return hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isFullHealth() {
        return hp >= MAX_HP;
    }

    public boolean isDisguised() {
        return disguiseTimer > 0f;
    }

    public float getDisguiseTimeRemaining() {
        return disguiseTimer;
    }

    public void activateDisguise() {
        disguiseTimer = DISGUISE_DURATION;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setFacing(float dx, float dy) {
        if (dx == 0f && dy == 0f) {
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            facingX = Math.signum(dx);
            facingY = 0f;
        } else {
            facingX = 0f;
            facingY = Math.signum(dy);
        }
    }

    public String getActiveItem() {
        return activeItem;
    }

    public String getInventoryText() {
        if (inventory.isEmpty()) {
            return "empty";
        }

        return String.join(", ", inventory);
    }

    public boolean hasItem(String itemType) {
        return inventory.contains(itemType);
    }

    public boolean isActiveItem(String itemType) {
        return itemType != null && itemType.equals(activeItem);
    }

    public boolean selectItem(String itemType) {
        if (!inventory.contains(itemType)) {
            return false;
        }

        activeItem = itemType;
        return true;
    }

    public void setActiveItem(String itemType) {
        inventory.add(itemType);
        activeItem = itemType;
    }

    public void consumeItem(String itemType) {
        inventory.remove(itemType);

        if (itemType.equals(activeItem)) {
            activeItem = null;

            for (String item : inventory) {
                activeItem = item;
            }
        }
    }

    public void moveTo(float newX, float newY) {
        x = newX;
        y = newY;
        bounds.setPosition(x, y);
    }

    public void takeDamage(int amount) {
        setHp(hp - amount);
    }

    public void heal(int amount) {
        setHp(hp + amount);
        healEffectTimer = HEAL_EFFECT_DURATION;
    }

    public void restoreHp(int value) {
        setHp(value);
    }

    public void addHPObserver(HPObserver observer) {
        if (observer != null && !hpObservers.contains(observer)) {
            hpObservers.add(observer);
            observer.onHPChanged(hp);
        }
    }

    public void removeHPObserver(HPObserver observer) {
        hpObservers.remove(observer);
    }

    private void setHp(int value) {
        int nextHp = Math.min(MAX_HP, Math.max(0, value));

        if (hp == nextHp) {
            return;
        }

        hp = nextHp;
        notifyHPObservers();
    }

    private void notifyHPObservers() {
        for (HPObserver observer : hpObservers) {
            observer.onHPChanged(hp);
        }
    }
}
