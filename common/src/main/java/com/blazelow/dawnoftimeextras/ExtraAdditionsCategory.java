package com.blazelow.dawnoftimeextras;

/**
 * Which Dawn Of Time cultural/design family a piece of this addon's own content was derived
 * from - the same five that this addon currently has content for, out of Dawn Of Time's own
 * seven (Roman and Chinese are deliberately absent: nothing in this addon derives from either).
 *
 * <p>This is the explicit, hand-maintained source of truth every {@code register(...)} call
 * states directly - never inferred from a registry id or material name at runtime. When a
 * future block is added, its category is a design decision made once, at the call site, the
 * same way every other one here was: trace which Dawn Of Time source model/texture it was
 * built from, then look up which of Dawn Of Time's own categories that source belongs to (see
 * their own {@code CreativeInventoryCategories} enum, decompiled, for the authoritative list).
 *
 * <p>{@link #dotName} selects the category's Dawn Of Time logo texture and tooltip (existing DoT assets).
 */
public enum ExtraAdditionsCategory {
    /** Tatami, futons, irori, the whole wood batch (timber frame, roof support, legless chair,
     * glass pane, fancy railing, window, shutters, the paper wall family, sliding paper door,
     * paper door, foundation, boards), paper lanterns, and the charred_spruce/red_painted
     * timber-frame signs - all derived from Dawn Of Time's own "japanese" model family. */
    JAPANESE("japanese"),
    /** Vanilla-wood balusters (waxed_oak_baluster), the whole masonry family, chimneys, the
     * German-design fireplace, and the fountain (pool/small pool/water jet/faucet) - all
     * derived from Dawn Of Time's own "german" model family. */
    GERMAN("german"),
    /** Sided columns (every material, stone and wood alike), stone-material balusters, the
     * French-design fireplace, gargoyles, and the reinforced wrought iron fences - all derived
     * from Dawn Of Time's own "french" model family (limestone's own designs). */
    FRENCH("french"),
    /** Crenelations - derived from Dawn Of Time's own "persian" sandstone crenelation. */
    PERSIAN("persian"),
    /** The plain (plastered-column-shaped) Column, painted stone, and the painted wave family -
     * derived from Dawn Of Time's own "precolumbian" model family. */
    PRE_COLOMBIAN("pre_columbian");

    /** Dawn Of Time's own name for this category: selects their {@code textures/item/logo_<name>.png}
     * button icon and their {@code gui.dawnoftimebuilder.<name>} tooltip key (both existing DoT assets). */
    public final String dotName;

    ExtraAdditionsCategory(String dotName) {
        this.dotName = dotName;
    }
}
