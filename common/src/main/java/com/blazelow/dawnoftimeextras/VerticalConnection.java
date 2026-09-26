package com.blazelow.dawnoftimeextras;

import net.minecraft.util.StringRepresentable;

public enum VerticalConnection implements StringRepresentable {

    NONE("none"),

    UNDER("under"),

    ABOVE("above"),

    BOTH("both");

    private final String name;

    VerticalConnection(String name) {
        this.name = name;
    }

    public static VerticalConnection of(boolean joinedAbove, boolean joinedBelow) {
        if (joinedAbove && joinedBelow) {
            return BOTH;
        }
        if (joinedAbove) {
            return ABOVE;
        }
        return joinedBelow ? UNDER : NONE;
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
