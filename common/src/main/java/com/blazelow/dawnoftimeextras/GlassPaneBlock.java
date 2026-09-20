package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A connecting glass pane with a wood frame - Dawn Of Time's own glass pane, in another wood.
 *
 * <p>Nothing here reimplements it: the vanilla-style connecting geometry (post/side/noside,
 * the four directional booleans) and connecting with other Dawn Of Time panes specifically
 * (not just anything tagged as glass) are both theirs, because this extends their block.
 */
public class GlassPaneBlock extends org.dawnoftime.dawnoftime.block.templates.PaneBlockDoT {
    public static final MapCodec<GlassPaneBlock> CODEC = simpleCodec(GlassPaneBlock::new);

    public GlassPaneBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
