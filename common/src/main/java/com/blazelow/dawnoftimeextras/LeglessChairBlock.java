package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

/**
 * A floor-height chair with no legs of its own - Dawn Of Time's own legless chair, in another
 * wood.
 *
 * <p>Nothing here reimplements it: sitting itself (the entity it spawns and rides the player
 * on), the facing/waterlogged state, and the collision shape are all theirs, because this
 * extends their block rather than copying it. The shape is the same physical footprint
 * regardless of material - the model's own dimensions never change between woods, confirmed
 * directly in Dawn Of Time's model JSON - so every wood reuses their one
 * {@code SPRUCE_LEGLESS_CHAIR_SHAPES} array rather than each getting its own copy.
 */
public class LeglessChairBlock extends org.dawnoftime.dawnoftime.block.templates.ChairBlock {
    public static final MapCodec<LeglessChairBlock> CODEC = simpleCodec(LeglessChairBlock::new);

    public LeglessChairBlock(BlockBehaviour.Properties properties) {
        super(properties, 7.0F, VoxelShapes.SPRUCE_LEGLESS_CHAIR_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
