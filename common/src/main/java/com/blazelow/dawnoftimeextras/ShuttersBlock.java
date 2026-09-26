package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

public class ShuttersBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceShuttersBlock {
    public static final MapCodec<ShuttersBlock> CODEC = simpleCodec(ShuttersBlock::new);

    public ShuttersBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.CHARRED_SPRUCE_SHUTTERS_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
