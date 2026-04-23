package com.team.mazerunner.world;

import com.team.mazerunner.entities.Player;

public class LockedDoor extends Door {

    private final Door door;
    private final String requiredItem;

    public LockedDoor(Door door, String requiredItem) {
        this.door = door;
        this.requiredItem = requiredItem;
    }

    @Override
    public boolean open(Player player) {
        if (!player.isActiveItem(requiredItem)) {
            return false;
        }

        player.consumeItem(requiredItem);
        return door.open(player);
    }
}
