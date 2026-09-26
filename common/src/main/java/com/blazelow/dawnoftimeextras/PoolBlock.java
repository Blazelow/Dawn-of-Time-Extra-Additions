package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

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
