package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoardsSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public static final MapCodec<BoardsSlabBlock> CODEC = simpleCodec(BoardsSlabBlock::new);

    public BoardsSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }
}
