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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public class SidedColumnBlock extends Block {
    public static final MapCodec<SidedColumnBlock> CODEC = simpleCodec(SidedColumnBlock::new);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION =
            EnumProperty.create("vertical_connection", VerticalConnection.class);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.NORTH, Block.box(1, 0, 9, 15, 16, 16));
        SHAPES.put(Direction.SOUTH, Block.box(1, 0, 0, 15, 16, 7));
        SHAPES.put(Direction.EAST, Block.box(0, 0, 1, 7, 16, 15));
        SHAPES.put(Direction.WEST, Block.box(9, 0, 1, 16, 16, 15));
    }

    public SidedColumnBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, VERTICAL_CONNECTION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state.setValue(VERTICAL_CONNECTION, connectionAt(state, context.getLevel(), context.getClickedPos()));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (direction.getAxis().isVertical()) {
            return state.setValue(VERTICAL_CONNECTION, connectionAt(state, level, pos));
        }
        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    private VerticalConnection connectionAt(BlockState state, BlockGetter level, BlockPos pos) {
        return VerticalConnection.of(joins(state, level.getBlockState(pos.above())),
                                     joins(state, level.getBlockState(pos.below())));
    }

    private boolean joins(BlockState state, BlockState neighbour) {
        return neighbour.is(this) && neighbour.getValue(FACING) == state.getValue(FACING);
    }

    private Predicate<BlockState> kin(BlockState state) {
        return neighbour -> joins(state, neighbour);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return ColumnGrowth.shrink(level, pos, player,
                    state.getValue(VERTICAL_CONNECTION) != VerticalConnection.NONE, kin(state));
        }
        return ColumnGrowth.grow(this, level, pos, player,
                this.defaultBlockState().setValue(FACING, state.getValue(FACING)), kin(state));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
