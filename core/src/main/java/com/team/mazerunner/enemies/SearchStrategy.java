package com.team.mazerunner.enemies;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class SearchStrategy implements MovementStrategy {

    @Override
    public void move(Enemy enemy, Player player, LevelMap levelMap, float delta) {
        enemy.searchLastKnownPosition(levelMap, delta);
    }
}
