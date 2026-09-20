package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The slab half of the column-style foundation - Dawn Of Time's own
 * {@code charred_spruce_foundation_slab}, in another wood.
 *
 * <p>A bare {@code SlabBlockDoT} (vanilla's own slab behaviour plus Dawn Of Time's fire
 * tracking) with fire spread/destruction speeds set after construction, same as
 * {@link FoundationBlock}. Only the column-style foundation gets a slab - the plain design
 * never had one on Dawn Of Time's own side either.
 */
public class FoundationSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public static final MapCodec<FoundationSlabBlock> CODEC = simpleCodec(FoundationSlabBlock::new);

    public FoundationSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }
}
