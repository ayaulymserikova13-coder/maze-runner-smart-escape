package com.team.mazerunner.enemies;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class ChaseStrategy implements MovementStrategy {

    @Override
    public void move(Enemy enemy, Player player, LevelMap levelMap, float delta) {
        enemy.moveToward(
                enemy.getLastKnownPlayerX(),
                enemy.getLastKnownPlayerY(),
                enemy.getChaseSpeed(),
                levelMap,
                delta
        );
    }
}
