package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PaperWallBlock extends org.dawnoftime.dawnoftime.block.templates.BottomPaneBlock {
    public static final MapCodec<PaperWallBlock> CODEC = simpleCodec(PaperWallBlock::new);

    public PaperWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
