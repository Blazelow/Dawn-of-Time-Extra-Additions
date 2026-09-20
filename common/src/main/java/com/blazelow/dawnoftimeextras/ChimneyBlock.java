package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

/**
 * A chimney stack - Dawn Of Time's own, in another stone. Their shapes too: it stacks, grows by
 * hand, starts lit, and takes a lighter, fireball, snowball or thrown water, all from theirs.
 */
public class ChimneyBlock extends org.dawnoftime.dawnoftime.block.templates.ChimneyBlockDoT {
    public static final MapCodec<ChimneyBlock> CODEC = simpleCodec(ChimneyBlock::new);

    public ChimneyBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.STONE_BRICKS_CHIMNEY_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
