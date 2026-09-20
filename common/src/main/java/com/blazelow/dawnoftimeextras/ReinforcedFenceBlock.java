package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A reinforced wrought iron fence, matching Dawn Of Time's.
 *
 * <p>It does both things their version does at once: it turns corners like stairs do, and it
 * grows upwards by hand. So {@code facing} and {@code shape} pick the corner piece while
 * {@code vertical_connection} picks the tier - a stack shows a plinth at the bottom, plain bars
 * in the middle and a capped top, without any of those being placed by hand.
 */
public class ReinforcedFenceBlock extends Block {
    public static final MapCodec<ReinforcedFenceBlock> CODEC = simpleCodec(ReinforcedFenceBlock::new);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION =
            EnumProperty.create("vertical_connection", VerticalConnection.class);

    /** The bars sit in a slab across the middle of the block, as Dawn Of Time's model does. */
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.SOUTH, Block.box(0, 0, 7, 16, 16, 15));
        SHAPES.put(Direction.NORTH, Block.box(0, 0, 1, 16, 16, 9));
        SHAPES.put(Direction.WEST, Block.box(1, 0, 0, 9, 16, 16));
        SHAPES.put(Direction.EAST, Block.box(7, 0, 0, 15, 16, 16));
    }

    private final Predicate<BlockState> kin = state -> state.is(this);

    public ReinforcedFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE, VERTICAL_CONNECTION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape shape = SHAPES.get(facing);
        // an inner corner carries fence along two sides, so collision has to cover both
        return switch (state.getValue(SHAPE)) {
            case INNER_LEFT -> Shapes.or(shape, SHAPES.get(facing.getCounterClockWise()));
            case INNER_RIGHT -> Shapes.or(shape, SHAPES.get(facing.getClockWise()));
            default -> shape;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
        BlockPos pos = context.getClickedPos();
        return state
                .setValue(SHAPE, StairShapes.of(state, context.getLevel(), pos, FACING, kin))
                .setValue(VERTICAL_CONNECTION, connectionAt(state, context.getLevel(), pos));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (direction.getAxis().isVertical()) {
            return state.setValue(VERTICAL_CONNECTION, connectionAt(state, level, pos));
        }
        return state.setValue(SHAPE, StairShapes.of(state, level, pos, FACING, kin));
    }

    private VerticalConnection connectionAt(BlockState state, BlockGetter level, BlockPos pos) {
        return VerticalConnection.of(kin.test(level.getBlockState(pos.above())),
                                     kin.test(level.getBlockState(pos.below())));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return ColumnGrowth.shrink(level, pos, player,
                    state.getValue(VERTICAL_CONNECTION) != VerticalConnection.NONE, kin);
        }
        return ColumnGrowth.grow(this, level, pos, player,
                this.defaultBlockState().setValue(FACING, state.getValue(FACING)), kin);
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
