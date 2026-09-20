package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.dawnoftime.util.VoxelShapes;

/**
 * A window shutter that opens on redstone or by hand - Dawn Of Time's own shutters, in
 * another wood.
 *
 * <p>Nothing here reimplements it: the facing/open/powered state, redstone handling, and
 * collision shape are all theirs, because this extends their block rather than copying it.
 * The shape is the same physical footprint regardless of material, so this reuses Dawn Of
 * Time's own {@code CHARRED_SPRUCE_SHUTTERS_SHAPES} array rather than each wood getting its
 * own copy.
 */
public class ShuttersBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceShuttersBlock {
    public static final MapCodec<ShuttersBlock> CODEC = simpleCodec(ShuttersBlock::new);

    public ShuttersBlock(BlockBehaviour.Properties properties) {
        super(properties, VoxelShapes.CHARRED_SPRUCE_SHUTTERS_SHAPES);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
