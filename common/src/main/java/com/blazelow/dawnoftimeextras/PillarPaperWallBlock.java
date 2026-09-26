package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PillarPaperWallBlock extends org.dawnoftime.dawnoftime.block.templates.PillarPaneBlock {
    public static final MapCodec<PillarPaperWallBlock> CODEC = simpleCodec(PillarPaperWallBlock::new);

    public PillarPaperWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
