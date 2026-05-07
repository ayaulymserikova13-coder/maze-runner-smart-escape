package com.team.mazerunner.enemies;

import com.badlogic.gdx.math.Vector2;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class PatrolStrategy implements MovementStrategy {

    @Override
    public void move(Enemy enemy, Player player, LevelMap levelMap, float delta) {
        if (enemy.isPatrolPaused(delta)) {
            return;
        }

        Vector2 target = enemy.getCurrentPatrolTarget();
        enemy.moveToward(target.x, target.y, enemy.getPatrolSpeed(), levelMap, delta);

        if (enemy.isCloseTo(target.x, target.y, 4f)) {
            enemy.advancePatrolTarget();
        }
    }
}
