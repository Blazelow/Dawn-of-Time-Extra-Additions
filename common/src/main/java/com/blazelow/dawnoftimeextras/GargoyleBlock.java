package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GargoyleBlock extends org.dawnoftime.dawnoftime.block.french.LimestoneGargoyleBlock {
    public static final MapCodec<GargoyleBlock> CODEC = simpleCodec(GargoyleBlock::new);

    public GargoyleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
