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
        Color borderColor = new Color(0.68f, 0.46f, 0.24f, 1f);

        shapeRenderer.setColor(new Color(0.015f, 0.012f, 0.010f, 1f));
        shapeRenderer.rect(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(bounds.x - 6, bounds.y - 6, bounds.width + 12, bounds.height + 6);
        shapeRenderer.rect(bounds.x - 6, bounds.y + bounds.height, bounds.width + 12, 6);
        shapeRenderer.rect(bounds.x - 6, bounds.y - 6, 6, bounds.height + 12);
        shapeRenderer.rect(bounds.x + bounds.width, bounds.y - 6, 6, bounds.height + 12);

        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.setColor(new Color(0.82f, 0.62f, 0.34f, 1f));
        shapeRenderer.rect(bounds.x + 6, bounds.y + bounds.height - 8, 8, 8);
        shapeRenderer.rect(bounds.x + bounds.width - 14, bounds.y + bounds.height - 8, 8, 8);
        shapeRenderer.rect(bounds.x + 6, bounds.y, 8, 8);
        shapeRenderer.rect(bounds.x + bounds.width - 14, bounds.y, 8, 8);
        shapeRenderer.setColor(new Color(0.24f, 0.58f, 0.58f, 1f));
        shapeRenderer.rect(bounds.x + 15, bounds.y + bounds.height - 6, bounds.width - 30, 3);
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
        if (label.toLowerCase().contains("retry") || label.toLowerCase().contains("play") ||
                label.toLowerCase().contains("resume")) {
            return hovered ? new Color(0.17f, 0.42f, 0.36f, 1f) : new Color(0.11f, 0.29f, 0.26f, 1f);
        }

        if (label.toLowerCase().contains("quit") || label.toLowerCase().contains("restart")) {
            return hovered ? new Color(0.43f, 0.16f, 0.14f, 1f) : new Color(0.30f, 0.11f, 0.10f, 1f);
        }

        return hovered ? new Color(0.14f, 0.34f, 0.42f, 1f) : new Color(0.10f, 0.23f, 0.31f, 1f);
    }
}
