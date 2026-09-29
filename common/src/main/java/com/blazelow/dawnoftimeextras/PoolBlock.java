package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class PoolBlock extends org.dawnoftime.dawnoftime.block.templates.PoolBlock {
    public PoolBlock(BlockBehaviour.Properties properties) {
        super(properties, 16, 14, VoxelShapes.POOL_SHAPES);
    }
}
