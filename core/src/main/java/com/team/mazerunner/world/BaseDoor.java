package com.team.mazerunner.world;

import com.team.mazerunner.entities.Player;

public class BaseDoor extends Door {

    @Override
    public boolean open(Player player) {
        return true;
    }
}
