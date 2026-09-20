package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The plain paper wall, the one with its own bottom-row model - Dawn Of Time's own
 * {@code BottomPaneBlock}, in another wood.
 *
 * <p>Nothing here reimplements it: the multipart connecting geometry (post/side/side_bottom/
 * noside) and the corner pillar are all theirs, because this extends their block. Only the
 * frame's own colour changes per wood; the corner pillar stays the same regardless of wood.
 */
public class PaperWallBlock extends org.dawnoftime.dawnoftime.block.templates.BottomPaneBlock {
    public static final MapCodec<PaperWallBlock> CODEC = simpleCodec(PaperWallBlock::new);

    public PaperWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
