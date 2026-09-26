package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PaperLanternColourBlock
        extends org.dawnoftime.dawnoftime.block.japanese.PaperLanternBlock {
    public static final MapCodec<PaperLanternColourBlock> CODEC =
            simpleCodec(PaperLanternColourBlock::new);

    public PaperLanternColourBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
