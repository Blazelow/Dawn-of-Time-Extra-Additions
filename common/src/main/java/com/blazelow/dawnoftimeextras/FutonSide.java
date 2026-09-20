package com.blazelow.dawnoftimeextras;

import net.minecraft.util.StringRepresentable;

/** Which half of a double futon a block is, seen from the foot looking towards the head. */
public enum FutonSide implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    FutonSide(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
