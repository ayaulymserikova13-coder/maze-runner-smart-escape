package com.team.mazerunner.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.items.Crowbar;
import com.team.mazerunner.items.Key;
import com.team.mazerunner.items.Knife;
import com.team.mazerunner.world.LevelMap;

public class PlayerInputHandler {

    private final Player player;
    private final LevelMap levelMap;

    public PlayerInputHandler(Player player, LevelMap levelMap) {
        this.player = player;
        this.levelMap = levelMap;
    }

    public void update(float delta) {
        float distance = player.getSpeed() * delta;
        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += distance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= distance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= distance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += distance;
        }

        if (dx != 0f || dy != 0f) {
            if (dx != 0f && dy != 0f) {
                Vector2 direction = new Vector2(dx, dy).nor().scl(distance);
                dx = direction.x;
                dy = direction.y;
            }

            new MoveCommand(player, levelMap, dx, dy).execute();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            new InteractCommand(player, levelMap).execute();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            new StealthKillCommand(player, levelMap).execute();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            levelMap.selectPlayerItem(player, Key.TYPE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            levelMap.selectPlayerItem(player, Crowbar.TYPE);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            levelMap.selectPlayerItem(player, Knife.TYPE);
        }
    }
}
