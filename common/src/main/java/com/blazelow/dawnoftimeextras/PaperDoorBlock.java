package com.blazelow.dawnoftimeextras;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class PaperDoorBlock extends org.dawnoftime.dawnoftime.block.templates.CenteredDoorBlock {
    public PaperDoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.BAMBOO);
    }
}
