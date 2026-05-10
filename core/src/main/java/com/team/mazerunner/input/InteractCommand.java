package com.team.mazerunner.input;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class InteractCommand implements ICommand {

    private final Player player;
    private final LevelMap levelMap;

    public InteractCommand(Player player, LevelMap levelMap) {
        this.player = player;
        this.levelMap = levelMap;
    }

    @Override
    public void execute() {
        if (levelMap.pickUpNearbyItem(player)) {
            return;
        }

        if (levelMap.useActiveItem(player)) {
            return;
        }

        levelMap.openNearbyDoor(player);
    }
}
