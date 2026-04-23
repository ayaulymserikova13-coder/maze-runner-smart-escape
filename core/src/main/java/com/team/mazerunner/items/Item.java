package com.team.mazerunner.items;

import com.team.mazerunner.entities.Player;

public interface Item {

    String getType();

    void onPickup(Player player);
}
