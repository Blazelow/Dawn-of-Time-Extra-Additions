package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public abstract class CorneringBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    protected CorneringBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, StairsShape.STRAIGHT));
    }

    protected abstract VoxelShape sideShape(BlockState state, Direction side);

    protected abstract Predicate<BlockState> kin();

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape shape = sideShape(state, facing);

        return switch (state.getValue(SHAPE)) {
            case INNER_LEFT -> Shapes.or(shape, sideShape(state, facing.getCounterClockWise()));
            case INNER_RIGHT -> Shapes.or(shape, sideShape(state, facing.getClockWise()));
            default -> shape;
        };
    }

    protected Direction facingFor(BlockPlaceContext context) {
        return context.getHorizontalDirection();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, facingFor(context));
        return state.setValue(SHAPE,
                StairShapes.of(state, context.getLevel(), context.getClickedPos(), FACING, kin()));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(SHAPE, StairShapes.of(state, level, pos, FACING, kin()));
        }
        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(FACING);
        StairsShape shape = state.getValue(SHAPE);
        boolean crosswise = mirror == Mirror.LEFT_RIGHT
                ? facing.getAxis() == Direction.Axis.Z
                : facing.getAxis() == Direction.Axis.X;
        if (!crosswise) {
            return super.mirror(state, mirror);
        }
        BlockState flipped = state.rotate(Rotation.CLOCKWISE_180);
        return switch (shape) {
            case INNER_LEFT -> flipped.setValue(SHAPE, StairsShape.INNER_RIGHT);
            case INNER_RIGHT -> flipped.setValue(SHAPE, StairsShape.INNER_LEFT);
            case OUTER_LEFT -> flipped.setValue(SHAPE, StairsShape.OUTER_RIGHT);
            case OUTER_RIGHT -> flipped.setValue(SHAPE, StairsShape.OUTER_LEFT);
            default -> flipped.setValue(SHAPE, StairsShape.STRAIGHT);
        };
    }
}
