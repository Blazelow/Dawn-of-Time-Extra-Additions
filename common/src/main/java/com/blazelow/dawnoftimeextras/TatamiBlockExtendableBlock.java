package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TatamiBlockExtendableBlock extends Block {
    public static final MapCodec<TatamiBlockExtendableBlock> CODEC =
            simpleCodec(TatamiBlockExtendableBlock::new);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TatamiBlockExtendableBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState base = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        return connectionsAt(base, context.getLevel(), context.getClickedPos());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        BlockState placed = level.getBlockState(pos);
        if (!placed.is(this)) {
            return;
        }
        for (BlockPos p : positionsWithinTwo(pos)) {
            BlockState there = level.getBlockState(p);
            if (there.is(this)) {
                refresh(level, p, there);
            }
        }
    }

    private java.util.List<BlockPos> positionsWithinTwo(BlockPos pos) {
        java.util.List<BlockPos> positions = new java.util.ArrayList<>();
        positions.add(pos);
        for (Direction d1 : Direction.Plane.HORIZONTAL) {
            BlockPos one = pos.relative(d1);
            positions.add(one);
            for (Direction d2 : Direction.Plane.HORIZONTAL) {
                positions.add(one.relative(d2));
            }
        }
        return positions;
    }

    private void refresh(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockState recomputed = connectionsAt(state, level, pos);
        if (recomputed != state) {
            level.setBlock(pos, recomputed, 3);
        }
    }

    private BlockState connectionsAt(BlockState state, LevelAccessor level, BlockPos pos) {
        return state
                .setValue(NORTH, joins(level, pos, Direction.NORTH))
                .setValue(SOUTH, joins(level, pos, Direction.SOUTH))
                .setValue(EAST, joins(level, pos, Direction.EAST))
                .setValue(WEST, joins(level, pos, Direction.WEST));
    }

    private boolean joins(LevelAccessor level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos.relative(direction)).is(this);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        state = super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
        if (state.is(this) && direction.getAxis().isHorizontal()) {
            state = connectionsAt(state, level, pos);
        }
        return state;
    }
}
