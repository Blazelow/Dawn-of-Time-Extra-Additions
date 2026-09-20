package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A decorative pane-like railing that can also hang below a ledge - Dawn Of Time's own fancy
 * railing, in another wood.
 *
 * <p>Nothing here reimplements it: despite the material-specific name, Dawn Of Time's own
 * {@code CharredSpruceFancyRailingBlock} is already the shared class behind all three of their
 * own materials (charred_spruce, red_painted, waxed_acacia - confirmed directly in their
 * decompiled registry, not assumed from the class name), so this extends it the same way for
 * every new wood. The hanging state's own collision shape is the same physical footprint
 * regardless of material - Dawn Of Time's own three materials all reuse their one
 * {@code CHARRED_SPRUCE_FANCY_RAILING_SHAPES} array too.
 */
public class FancyRailingBlock extends org.dawnoftime.dawnoftime.block.japanese.CharredSpruceFancyRailingBlock {
    public static final MapCodec<FancyRailingBlock> CODEC = simpleCodec(FancyRailingBlock::new);

    public FancyRailingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
