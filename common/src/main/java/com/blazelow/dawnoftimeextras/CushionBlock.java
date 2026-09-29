package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class CushionBlock extends org.dawnoftime.dawnoftime.block.templates.ChairBlock {
    public CushionBlock(BlockBehaviour.Properties properties) {
        super(properties, 3.0F, VoxelShapes.WHITE_CUSHION_SHAPES);
    }
}
