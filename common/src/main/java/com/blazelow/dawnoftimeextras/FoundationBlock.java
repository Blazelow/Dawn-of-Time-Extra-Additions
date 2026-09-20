package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A plain flammable foundation block - Dawn Of Time's own {@code spruce_foundation} /
 * {@code charred_spruce_foundation}, in another wood.
 *
 * <p>Both of Dawn Of Time's own foundation designs (the plain cube and the log-ended column)
 * use this exact same class on their side - a bare {@code BlockDoT} with fire spread/
 * destruction speeds set after construction - so one wrapper here covers both. Nothing else is
 * overridden: the block has no functional state of its own, its four visible rotations are a
 * purely decorative blockstate trick (a weighted array of y-rotated variants under one empty
 * state key), not a block property.
 */
public class FoundationBlock extends org.dawnoftime.dawnoftime.block.templates.BlockDoT {
    public static final MapCodec<FoundationBlock> CODEC = simpleCodec(FoundationBlock::new);

    public FoundationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
