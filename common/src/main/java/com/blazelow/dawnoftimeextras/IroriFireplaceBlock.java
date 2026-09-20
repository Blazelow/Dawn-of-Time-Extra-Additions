package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A sunken hearth - Dawn Of Time's irori, in another wood. Their crackle, their embers, their
 * smoke; only the light it gives is ours, set on the block's properties where it is registered.
 */
public class IroriFireplaceBlock extends org.dawnoftime.dawnoftime.block.japanese.IroriFireplaceBlock {
    public static final MapCodec<IroriFireplaceBlock> CODEC = simpleCodec(IroriFireplaceBlock::new);

    public IroriFireplaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
