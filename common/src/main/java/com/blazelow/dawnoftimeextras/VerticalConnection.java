package com.blazelow.dawnoftimeextras;

import net.minecraft.util.StringRepresentable;

/**
 * What a stacking block is joined to, vertically. Mirrors the property Dawn Of Time uses on its
 * own columns, including the names, so the blockstate files read the same way theirs do.
 */
public enum VerticalConnection implements StringRepresentable {
    /** Standing alone. */
    NONE("none"),
    /** Joined to a block below it, so this is the top of the stack. */
    UNDER("under"),
    /** Joined to a block above it, so this is the foot of the stack. */
    ABOVE("above"),
    /** Joined at both ends, so this is a middle section. */
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
