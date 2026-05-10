package com.team.mazerunner.items;

import com.team.mazerunner.entities.Player;

public class Disguise implements Item {

    public static final String TYPE = "disguise";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void onPickup(Player player) {
        player.setActiveItem(TYPE);
    }
}
