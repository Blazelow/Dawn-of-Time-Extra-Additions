package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FoundationBlock extends org.dawnoftime.dawnoftime.block.templates.BlockDoT {
    public static final MapCodec<FoundationBlock> CODEC = simpleCodec(FoundationBlock::new);

    public FoundationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
