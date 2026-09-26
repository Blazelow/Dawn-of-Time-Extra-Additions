package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class PaperDoorBlock extends org.dawnoftime.dawnoftime.block.templates.CenteredDoorBlock {
    public static final MapCodec<PaperDoorBlock> CODEC = simpleCodec(PaperDoorBlock::new);

    public PaperDoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.BAMBOO);
    }

    @Override
    public MapCodec<? extends DoorBlock> codec() {
        return CODEC;
    }
}
