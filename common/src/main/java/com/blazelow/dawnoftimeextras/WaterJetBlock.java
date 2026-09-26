package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WaterJetBlock extends org.dawnoftime.dawnoftime.block.templates.WaterJetBlock {
    public static final MapCodec<WaterJetBlock> CODEC = simpleCodec(WaterJetBlock::new);

    public WaterJetBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
