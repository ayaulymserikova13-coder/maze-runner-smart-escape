package com.team.mazerunner.items;

import com.team.mazerunner.entities.Player;

public class Medkit implements Item {

    public static final String TYPE = "medkit";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void onPickup(Player player) {
        player.heal(1);
    }
}
