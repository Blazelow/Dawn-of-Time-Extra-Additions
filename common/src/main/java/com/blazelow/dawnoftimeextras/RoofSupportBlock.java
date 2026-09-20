package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * A diagonal roof brace that joins into a matching neighbour - Dawn Of Time's own roof
 * support, in another wood.
 *
 * <p>Nothing here reimplements it: the bottom/top/double placement, the corner-joining shape,
 * and the "right-click with a grey roof tile slab to cap it" interaction are all theirs,
 * because this extends their block rather than copying it. The tile-slab reference is the
 * same block every one of Dawn Of Time's own four materials' roof supports use (confirmed
 * directly in their decompiled registry - charred_spruce, red_painted, spruce and
 * waxed_acacia all point at their own gray_roof_tiles_slab, never a material-matched one), so
 * this does too rather than inventing a per-wood substitute.
 */
public class RoofSupportBlock extends org.dawnoftime.dawnoftime.block.templates.MixedRoofSupportBlock {
    public static final MapCodec<RoofSupportBlock> CODEC = simpleCodec(RoofSupportBlock::new);

    private static final Supplier<Block> GRAY_ROOF_TILES_SLAB = () -> BuiltInRegistries.BLOCK.get(
            ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder", "gray_roof_tiles_slab"));

    public RoofSupportBlock(BlockBehaviour.Properties properties) {
        super(GRAY_ROOF_TILES_SLAB, properties);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }
}
