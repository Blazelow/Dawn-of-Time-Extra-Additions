package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class CushionBlock extends org.dawnoftime.dawnoftime.block.templates.ChairBlock {
    public static final MapCodec<CushionBlock> CODEC = simpleCodec(CushionBlock::new);

    public CushionBlock(BlockBehaviour.Properties properties) {
        super(properties, 3.0F, VoxelShapes.WHITE_CUSHION_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
