package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The two-block-tall version of {@link ShuttersBlock} - Dawn Of Time's own tall shutters, in
 * another wood.
 *
 * <p>Nothing here reimplements it: {@code CharredSpruceTallShuttersBlock} already hardcodes
 * its own (material-independent) shape internally, so the only thing this wrapper needs to
 * supply is properties, same pattern as every other wrapper in this batch.
 */
public class TallShuttersBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceTallShuttersBlock {
    public static final MapCodec<TallShuttersBlock> CODEC = simpleCodec(TallShuttersBlock::new);

    public TallShuttersBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
