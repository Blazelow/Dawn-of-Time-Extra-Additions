package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoardsBlock extends org.dawnoftime.dawnoftime.block.templates.RotatedPillarBlockDoT {
    public BoardsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable();
    }
}
