package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

/**
 * A basin that holds water - Dawn Of Time's own pool, in another stone.
 *
 * <p>Nothing here reimplements it. The levels, the way neighbouring basins share one body of
 * water, the bucket and bottle handling, the pillar, what counts as a source overhead: all of
 * that is theirs, because this extends their block rather than copying it. The arguments are
 * the ones they build theirs with - sixteen levels, and a faucet that closes at fourteen.
 */
public class PoolBlock extends org.dawnoftime.dawnoftime.block.templates.PoolBlock {
    public static final MapCodec<PoolBlock> CODEC = simpleCodec(PoolBlock::new);

    public PoolBlock(BlockBehaviour.Properties properties) {
        super(properties, 16, 14, VoxelShapes.POOL_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
