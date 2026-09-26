package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SmallPoolBlock extends org.dawnoftime.dawnoftime.block.templates.SmallPoolBlock {
    public static final MapCodec<SmallPoolBlock> CODEC = simpleCodec(SmallPoolBlock::new);

    public SmallPoolBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
