package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class LeglessChairBlock extends org.dawnoftime.dawnoftime.block.templates.ChairBlock {
    public static final MapCodec<LeglessChairBlock> CODEC = simpleCodec(LeglessChairBlock::new);

    public LeglessChairBlock(BlockBehaviour.Properties properties) {
        super(properties, 7.0F, VoxelShapes.SPRUCE_LEGLESS_CHAIR_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
