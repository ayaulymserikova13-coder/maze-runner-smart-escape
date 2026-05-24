package com.team.mazerunner.items;

public class ItemFactory {

    private ItemFactory() {
    }

    public static Item create(String type) {
        if (Key.TYPE.equals(type)) {
            return new Key();
        }

        if (Crowbar.TYPE.equals(type)) {
            return new Crowbar();
        }

        if (Knife.TYPE.equals(type)) {
            return new Knife();
        }

        if (Disguise.TYPE.equals(type)) {
            return new Disguise();
        }

        if (Medkit.TYPE.equals(type)) {
            return new Medkit();
        }

        throw new IllegalArgumentException("Unknown item type: " + type);
    }
}
