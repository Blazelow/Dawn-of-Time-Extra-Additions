package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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

    /**
     * A fireplace is two blocks tall - the hearth with the fire, and the hood above it - but Dawn Of Time only
     * ever sets "lit" on the hearth, so only the hearth gave off light and the hood stayed dark (reported by
     * Blazelow). A fireplace piece standing on another one now copies that piece's lit state, so the hood
     * glows with the fire. Shape updates reach the upper block whenever the lower one changes.
     */
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockState result = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction == Direction.DOWN
                && neighborState.getBlock() instanceof org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalSidedPlanFireplaceBlock
                && result.getBlock() instanceof org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalSidedPlanFireplaceBlock
                && neighborState.hasProperty(BlockStateProperties.LIT)
                && result.hasProperty(BlockStateProperties.LIT)
                && result.getValue(BlockStateProperties.LIT) != neighborState.getValue(BlockStateProperties.LIT)) {
            result = result.setValue(BlockStateProperties.LIT, neighborState.getValue(BlockStateProperties.LIT));
        }
        return result;
    }
}
