package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FancyRailingBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceFancyRailingBlock {
    public static final MapCodec<FancyRailingBlock> CODEC = simpleCodec(FancyRailingBlock::new);

    public FancyRailingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
