package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class VerticalPillarPaperWallBlock extends org.dawnoftime.dawnoftime.block.templates.PillarPaneBlock {
    public static final MapCodec<VerticalPillarPaperWallBlock> CODEC = simpleCodec(VerticalPillarPaperWallBlock::new);

    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION =
            EnumProperty.create("vertical_connection", VerticalConnection.class);

    public VerticalPillarPaperWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VERTICAL_CONNECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }
        return state.setValue(VERTICAL_CONNECTION, connectionAt(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        BlockState updated = super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
        if (direction.getAxis().isVertical()) {
            return updated.setValue(VERTICAL_CONNECTION, connectionAt(level, pos));
        }
        return updated;
    }

    private VerticalConnection connectionAt(LevelAccessor level, BlockPos pos) {
        return VerticalConnection.of(level.getBlockState(pos.above()).is(this),
                                     level.getBlockState(pos.below()).is(this));
    }
}
