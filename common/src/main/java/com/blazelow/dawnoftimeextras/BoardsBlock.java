package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoardsBlock extends org.dawnoftime.dawnoftime.block.templates.RotatedPillarBlockDoT {
    public static final MapCodec<BoardsBlock> CODEC = simpleCodec(BoardsBlock::new);

    public BoardsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable();
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }
}
