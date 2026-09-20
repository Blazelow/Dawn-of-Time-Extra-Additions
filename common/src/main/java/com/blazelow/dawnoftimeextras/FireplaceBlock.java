package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A hearth built into a wall - Dawn Of Time's fireplace, in another stone.
 *
 * <p>Everything it does is theirs: the piece it draws for each neighbour, lighting it with a
 * lighter, fireball or flaming arrow, putting it out with a snowball or a water potion, and
 * telling the chimneys above whether they are drawing smoke. Their own tooltip comes with it,
 * so this one is deliberately absent from {@link DawnOfTimeExtras#TOOLTIPS} - adding ours would
 * print the same three lines twice.
 */
public class FireplaceBlock
        extends org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalSidedPlanFireplaceBlock {
    public static final MapCodec<FireplaceBlock> CODEC = simpleCodec(FireplaceBlock::new);

    public FireplaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
