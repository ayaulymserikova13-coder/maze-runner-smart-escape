package com.team.mazerunner.input;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class StealthKillCommand implements ICommand {

    private final Player player;
    private final LevelMap levelMap;

    public StealthKillCommand(Player player, LevelMap levelMap) {
        this.player = player;
        this.levelMap = levelMap;
    }

    @Override
    public void execute() {
        levelMap.stealthKillNearbyEnemy(player);
    }
}
