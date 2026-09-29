package com.blazelow.dawnoftimeextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class RoofSupportBlock extends org.dawnoftime.dawnoftime.block.templates.MixedRoofSupportBlock {
    private static final Supplier<Block> GRAY_ROOF_TILES_SLAB = () -> BuiltInRegistries.BLOCK.get(
            new ResourceLocation("dawnoftimebuilder", "gray_roof_tiles_slab"));

    public RoofSupportBlock(BlockBehaviour.Properties properties) {
        super(GRAY_ROOF_TILES_SLAB, properties);
    }
}
