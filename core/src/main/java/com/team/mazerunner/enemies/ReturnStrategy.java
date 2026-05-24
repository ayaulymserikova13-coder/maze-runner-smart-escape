package com.team.mazerunner.enemies;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class ReturnStrategy implements MovementStrategy {

    @Override
    public void move(Enemy enemy, Player player, LevelMap levelMap, float delta) {
        enemy.moveToward(enemy.getStartX(), enemy.getStartY(), enemy.getPatrolSpeed(), levelMap, delta);

        if (enemy.isCloseTo(enemy.getStartX(), enemy.getStartY(), 4f)) {
            enemy.setState(EnemyState.PATROL);
        }
    }
}
