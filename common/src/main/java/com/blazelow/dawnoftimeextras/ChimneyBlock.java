package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class ChimneyBlock extends org.dawnoftime.dawnoftime.block.templates.ChimneyBlockDoT {
    public static final MapCodec<ChimneyBlock> CODEC = simpleCodec(ChimneyBlock::new);

    public ChimneyBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.STONE_BRICKS_CHIMNEY_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
