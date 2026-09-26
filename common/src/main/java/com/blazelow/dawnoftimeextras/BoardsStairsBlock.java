package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class BoardsStairsBlock extends org.dawnoftime.dawnoftime.block.templates.StairsBlockDoT {
    private final MapCodec<BoardsStairsBlock> codec;

    public BoardsStairsBlock(Supplier<Block> baseBlock, BlockBehaviour.Properties properties) {
        super(baseBlock, properties);
        this.codec = simpleCodec(p -> new BoardsStairsBlock(baseBlock, p));
    }

    @Override
    public MapCodec<? extends StairBlock> codec() {
        return this.codec;
    }
}
