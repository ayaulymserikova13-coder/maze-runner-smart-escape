package com.team.mazerunner.items;

import com.team.mazerunner.entities.Player;

public class Key implements Item {

    public static final String TYPE = "key";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void onPickup(Player player) {
        player.setActiveItem(TYPE);
    }
}
