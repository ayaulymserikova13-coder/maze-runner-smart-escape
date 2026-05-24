package com.team.mazerunner.enemies;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class ChaseStrategy implements MovementStrategy {

    @Override
    public void move(Enemy enemy, Player player, LevelMap levelMap, float delta) {
        float targetX = enemy.getLastKnownPlayerX();
        float targetY = enemy.getLastKnownPlayerY();

        if (enemy.isTargetVisible()) {
            targetX = levelMap.getTileCenterX(player.getX() + player.getWidth() / 2f);
            targetY = levelMap.getTileCenterY(player.getY() + player.getHeight() / 2f);
        }

        enemy.moveToward(
                targetX,
                targetY,
                enemy.getChaseSpeed(),
                levelMap,
                delta
        );

        enemy.finishChaseIfAtLastKnownPosition(delta);
    }
}
