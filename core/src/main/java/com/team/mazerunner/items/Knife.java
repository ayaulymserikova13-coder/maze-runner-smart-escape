package com.team.mazerunner.items;

import com.team.mazerunner.entities.Player;

public class Knife implements Item {

    public static final String TYPE = "knife";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void onPickup(Player player) {
        player.setActiveItem(TYPE);
    }
}
