package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The red paper lantern - Dawn Of Time's own {@code red_paper_lantern}, in every other vanilla
 * dye colour.
 *
 * <p>Named to avoid colliding with Dawn Of Time's own {@code PaperLanternBlock} (a real class
 * name clash, not just a similar one - this extends it directly rather than reimplementing it).
 * Nothing overridden: the lit-paper glow, waterlogging and display shape are all theirs.
 */
public class PaperLanternColourBlock
        extends org.dawnoftime.dawnoftime.block.japanese.PaperLanternBlock {
    public static final MapCodec<PaperLanternColourBlock> CODEC =
            simpleCodec(PaperLanternColourBlock::new);

    public PaperLanternColourBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
