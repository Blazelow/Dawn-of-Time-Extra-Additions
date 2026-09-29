package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class BoardsStairsBlock extends org.dawnoftime.dawnoftime.block.templates.StairsBlockDoT {
    public BoardsStairsBlock(Supplier<Block> baseBlock, BlockBehaviour.Properties properties) {
        super(baseBlock, properties);
    }
}
