package com.team.mazerunner.world;

public class ArrayLevelMapAdapter implements ILevelMap {

    private final int[][] tiles;

    public ArrayLevelMapAdapter(int[][] tiles) {
        this.tiles = tiles;
    }

    @Override
    public int getTile(int tileX, int tileY) {
        return tiles[tileY][tileX];
    }

    @Override
    public int getWidth() {
        return tiles[0].length;
    }

    @Override
    public int getHeight() {
        return tiles.length;
    }
}
