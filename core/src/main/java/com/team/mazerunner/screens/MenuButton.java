package com.team.mazerunner.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class MenuButton {

    private final Rectangle bounds;
    private final String label;
    private final GlyphLayout layout = new GlyphLayout();

    public MenuButton(String label, float x, float y, float width, float height) {
        this.label = label;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void drawShape(ShapeRenderer shapeRenderer, boolean hovered) {
        Color fillColor = getFillColor(hovered);
        Color borderColor = getBorderColor();
        Color lineColor = getLineColor();

        shapeRenderer.setColor(new Color(0.004f, 0.006f, 0.008f, 1f));
        shapeRenderer.rect(bounds.x - 7, bounds.y - 7, bounds.width + 14, bounds.height + 14);
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(bounds.x - 5, bounds.y - 5, bounds.width + 10, 5);
        shapeRenderer.rect(bounds.x - 5, bounds.y + bounds.height, bounds.width + 10, 5);
        shapeRenderer.rect(bounds.x - 5, bounds.y - 5, 5, bounds.height + 10);
        shapeRenderer.rect(bounds.x + bounds.width, bounds.y - 5, 5, bounds.height + 10);

        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.setColor(new Color(1f, 1f, 1f, hovered ? 0.16f : 0.08f));
        shapeRenderer.rect(bounds.x + 3, bounds.y + bounds.height - 9, bounds.width - 6, 4);

        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(bounds.x + 7, bounds.y + bounds.height - 8, 7, 7);
        shapeRenderer.rect(bounds.x + bounds.width - 14, bounds.y + bounds.height - 8, 7, 7);
        shapeRenderer.rect(bounds.x + 7, bounds.y + 1, 7, 7);
        shapeRenderer.rect(bounds.x + bounds.width - 14, bounds.y + 1, 7, 7);
        shapeRenderer.setColor(lineColor);
        shapeRenderer.rect(bounds.x + 26, bounds.y + bounds.height - 6, bounds.width - 52, 3);
        drawIcon(shapeRenderer, lineColor);
    }

    public void drawText(SpriteBatch batch, BitmapFont font) {
        font.setColor(Color.WHITE);
        layout.setText(font, label);
        font.draw(
                batch,
                label,
                bounds.x + bounds.width / 2f - layout.width / 2f,
                bounds.y + bounds.height / 2f + layout.height / 2f
        );
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    private Color getFillColor(boolean hovered) {
        String lower = label.toLowerCase();
        if (lower.contains("retry") || lower.contains("continue")) {
            return hovered ? new Color(0.74f, 0.42f, 0.04f, 1f) : new Color(0.52f, 0.31f, 0.04f, 1f);
        }

        if (lower.contains("play") || lower.contains("resume")) {
            return hovered ? new Color(0.06f, 0.48f, 0.43f, 1f) : new Color(0.05f, 0.32f, 0.30f, 1f);
        }

        if (lower.contains("quit") || lower.contains("restart")) {
            return hovered ? new Color(0.43f, 0.16f, 0.14f, 1f) : new Color(0.30f, 0.11f, 0.10f, 1f);
        }

        return hovered ? new Color(0.12f, 0.35f, 0.52f, 1f) : new Color(0.07f, 0.22f, 0.36f, 1f);
    }

    private Color getBorderColor() {
        String lower = label.toLowerCase();
        if (lower.contains("retry") || lower.contains("continue")) {
            return new Color(0.92f, 0.62f, 0.12f, 1f);
        }
        if (lower.contains("quit") || lower.contains("restart")) {
            return new Color(0.78f, 0.22f, 0.15f, 1f);
        }
        if (lower.contains("play") || lower.contains("resume")) {
            return new Color(0.12f, 0.80f, 0.74f, 1f);
        }
        return new Color(0.18f, 0.58f, 0.86f, 1f);
    }

    private Color getLineColor() {
        String lower = label.toLowerCase();
        if (lower.contains("retry") || lower.contains("continue")) {
            return new Color(1.00f, 0.78f, 0.24f, 1f);
        }
        if (lower.contains("quit") || lower.contains("restart")) {
            return new Color(0.95f, 0.34f, 0.24f, 1f);
        }
        if (lower.contains("play") || lower.contains("resume")) {
            return new Color(0.40f, 0.96f, 0.86f, 1f);
        }
        return new Color(0.45f, 0.76f, 1.00f, 1f);
    }

    private void drawIcon(ShapeRenderer shapeRenderer, Color color) {
        float iconX = bounds.x + 28;
        float iconY = bounds.y + bounds.height / 2f;
        String lower = label.toLowerCase();
        shapeRenderer.setColor(color);

        if (lower.contains("play")) {
            shapeRenderer.triangle(iconX - 6, iconY - 9, iconX - 6, iconY + 9, iconX + 10, iconY);
        } else if (lower.contains("quit")) {
            shapeRenderer.rect(iconX - 7, iconY - 2, 14, 4);
            shapeRenderer.rect(iconX - 2, iconY - 7, 4, 14);
        } else if (lower.contains("retry")) {
            shapeRenderer.circle(iconX, iconY, 8);
            shapeRenderer.setColor(getFillColor(false));
            shapeRenderer.circle(iconX + 2, iconY, 5);
            shapeRenderer.setColor(color);
            shapeRenderer.triangle(iconX + 2, iconY + 8, iconX + 11, iconY + 8, iconX + 8, iconY + 1);
        } else if (lower.contains("continue")) {
            shapeRenderer.triangle(iconX - 8, iconY - 8, iconX - 8, iconY + 8, iconX + 4, iconY);
            shapeRenderer.rect(iconX + 3, iconY - 3, 10, 6);
        } else {
            shapeRenderer.rect(iconX - 8, iconY - 7, 16, 12);
            shapeRenderer.triangle(iconX - 10, iconY - 6, iconX, iconY + 6, iconX + 10, iconY - 6);
        }
    }
}
