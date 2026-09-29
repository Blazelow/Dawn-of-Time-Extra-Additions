package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FireplaceBlock
        extends org.dawnoftime.dawnoftime.block.templates.ConnectedVerticalSidedPlanFireplaceBlock {
    public FireplaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

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
