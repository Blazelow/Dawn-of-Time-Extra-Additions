package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TallShuttersBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceTallShuttersBlock {
    public static final MapCodec<TallShuttersBlock> CODEC = simpleCodec(TallShuttersBlock::new);

    public TallShuttersBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
