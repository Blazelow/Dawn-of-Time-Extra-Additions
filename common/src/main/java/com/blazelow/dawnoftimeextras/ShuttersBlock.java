package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class ShuttersBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceShuttersBlock {
    public ShuttersBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.CHARRED_SPRUCE_SHUTTERS_SHAPES);
    }
}
