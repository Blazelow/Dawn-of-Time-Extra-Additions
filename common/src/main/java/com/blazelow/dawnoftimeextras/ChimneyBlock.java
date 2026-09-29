package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class ChimneyBlock extends org.dawnoftime.dawnoftime.block.templates.ChimneyBlockDoT {
    public ChimneyBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.STONE_BRICKS_CHIMNEY_SHAPES);
    }
}
