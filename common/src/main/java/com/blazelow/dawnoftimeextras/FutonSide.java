package com.blazelow.dawnoftimeextras;

import net.minecraft.util.StringRepresentable;

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
