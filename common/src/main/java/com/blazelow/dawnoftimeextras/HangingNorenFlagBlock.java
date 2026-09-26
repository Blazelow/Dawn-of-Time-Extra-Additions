package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingNorenFlagBlock extends Block {
    public static final MapCodec<HangingNorenFlagBlock> CODEC = simpleCodec(HangingNorenFlagBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty ABOVE = BooleanProperty.create("above");
    public static final BooleanProperty BELOW = BooleanProperty.create("below");

    public static final BooleanProperty NEG = BooleanProperty.create("neg");
    public static final BooleanProperty POS = BooleanProperty.create("pos");

    private static final VoxelShape SHAPE_Z = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape SHAPE_X = Block.box(7, 0, 0, 9, 16, 16);

    public HangingNorenFlagBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Z).setValue(ABOVE, false).setValue(BELOW, false)
                .setValue(NEG, false).setValue(POS, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, ABOVE, BELOW, NEG, POS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState above = context.getLevel().getBlockState(pos.above());
        BlockState below = context.getLevel().getBlockState(pos.below());
        Direction.Axis axis = context.getHorizontalDirection().getAxis();

        if (above.is(this)) {
            axis = above.getValue(AXIS);
        } else if (below.is(this)) {
            axis = below.getValue(AXIS);
        } else {
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockState next = context.getLevel().getBlockState(pos.relative(side));
                if (next.is(this) && side.getAxis() != next.getValue(AXIS)) {
                    axis = next.getValue(AXIS);
                    break;
                }
            }
        }
        BlockState state = defaultBlockState().setValue(AXIS, axis)
                .setValue(ABOVE, joins(axis, above)).setValue(BELOW, joins(axis, below));
        Direction pos_ = positive(axis);
        return state.setValue(POS, joins(axis, context.getLevel().getBlockState(pos.relative(pos_))))
                .setValue(NEG, joins(axis, context.getLevel().getBlockState(pos.relative(pos_.getOpposite()))));
    }

    private static Direction positive(Direction.Axis axis) {
        return axis == Direction.Axis.Z ? Direction.EAST : Direction.SOUTH;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis axis = state.getValue(AXIS);
        if (direction == Direction.UP) {
            return state.setValue(ABOVE, joins(axis, neighborState));
        }
        if (direction == Direction.DOWN) {
            return state.setValue(BELOW, joins(axis, neighborState));
        }
        if (direction == positive(axis)) {
            return state.setValue(POS, joins(axis, neighborState));
        }
        if (direction == positive(axis).getOpposite()) {
            return state.setValue(NEG, joins(axis, neighborState));
        }
        return state;
    }

    private boolean joins(Direction.Axis axis, BlockState neighbour) {
        return neighbour.is(this) && neighbour.getValue(AXIS) == axis;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }
}
