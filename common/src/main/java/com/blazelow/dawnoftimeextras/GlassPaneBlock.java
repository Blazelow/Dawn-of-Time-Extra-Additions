package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GlassPaneBlock extends org.dawnoftime.dawnoftime.block.templates.PaneBlockDoT {
    public static final MapCodec<GlassPaneBlock> CODEC = simpleCodec(GlassPaneBlock::new);

    public GlassPaneBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
