package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoardsSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public BoardsSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }
}
