package com.team.mazerunner.input;

import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

public class MoveCommand implements ICommand {

    private final Player player;
    private final LevelMap levelMap;
    private final float dx;
    private final float dy;

    public MoveCommand(Player player, LevelMap levelMap, float dx, float dy) {
        this.player = player;
        this.levelMap = levelMap;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        float nextX = player.getX() + dx;
        float nextY = player.getY() + dy;

        player.setFacing(dx, dy);

        if (!levelMap.isBlocked(nextX, player.getY(), player.getWidth(), player.getHeight())) {
            player.moveTo(nextX, player.getY());
        }

        if (!levelMap.isBlocked(player.getX(), nextY, player.getWidth(), player.getHeight())) {
            player.moveTo(player.getX(), nextY);
        }
    }
}
