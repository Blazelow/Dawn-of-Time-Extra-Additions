package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FoundationBlock extends org.dawnoftime.dawnoftime.block.templates.BlockDoT {
    public FoundationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }
}
