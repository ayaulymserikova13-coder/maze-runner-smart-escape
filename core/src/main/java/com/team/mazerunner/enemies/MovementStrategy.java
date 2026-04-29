package com.team.mazerunner.enemies;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public interface MovementStrategy {

    void move(Enemy enemy, Player player, LevelMap levelMap, float delta);
}
