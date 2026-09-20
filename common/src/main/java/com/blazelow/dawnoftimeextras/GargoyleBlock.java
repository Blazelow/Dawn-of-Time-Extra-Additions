package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A waterspout carved as a gargoyle - Dawn Of Time's own, in another stone. The rain filling it,
 * the drip as it dries, and the lighter that fixes the flow are all theirs.
 */
public class GargoyleBlock extends org.dawnoftime.dawnoftime.block.french.LimestoneGargoyleBlock {
    public static final MapCodec<GargoyleBlock> CODEC = simpleCodec(GargoyleBlock::new);

    public GargoyleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
