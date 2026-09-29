package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FoundationSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public FoundationSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }
}
