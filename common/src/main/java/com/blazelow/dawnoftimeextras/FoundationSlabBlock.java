package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FoundationSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public static final MapCodec<FoundationSlabBlock> CODEC = simpleCodec(FoundationSlabBlock::new);

    public FoundationSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }
}
