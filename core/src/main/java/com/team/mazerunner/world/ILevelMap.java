package com.team.mazerunner.world;

public interface ILevelMap {
    int getTile(int tileX, int tileY);

    int getWidth();

    int getHeight();

    default int getPixelWidth() {
        return getWidth() * LevelMap.TILE_SIZE;
    }

    default int getPixelHeight() {
        return getHeight() * LevelMap.TILE_SIZE;
    }
}
