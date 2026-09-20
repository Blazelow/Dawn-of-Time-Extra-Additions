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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

/**
 * A free-standing column that grows by hand, matching Dawn Of Time's plastered stone column.
 *
 * <p>Simpler than {@link SidedColumnBlock}: it stands in the middle of its block rather than
 * against a wall, so it has no facing - only what it is joined to, which picks its piece. A lone
 * column gets base and capital in one, a stack gets a foot, plain shafts and a capital, none of
 * which are placed by hand.
 *
 * <p>Right-clicking while holding another adds a section on top (consumed unless in creative);
 * sneaking and right-clicking takes the top one back off, exactly as Dawn Of Time's does.
 */
public class ColumnBlock extends Block {
    public static final MapCodec<ColumnBlock> CODEC = simpleCodec(ColumnBlock::new);

    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION =
            EnumProperty.create("vertical_connection", VerticalConnection.class);

    /** The shaft is slimmer than the base and capital, which flare out. */
    private static final VoxelShape SHAFT = Block.box(3, 0, 3, 13, 16, 13);
    private static final VoxelShape FLARED = Block.box(1, 0, 1, 15, 16, 15);

    private final Predicate<BlockState> kin = state -> state.is(this);

    public ColumnBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_CONNECTION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(VERTICAL_CONNECTION) == VerticalConnection.BOTH ? SHAFT : FLARED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(VERTICAL_CONNECTION, connectionAt(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        if (direction.getAxis().isVertical()) {
            return state.setValue(VERTICAL_CONNECTION, connectionAt(level, pos));
        }
        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    private VerticalConnection connectionAt(BlockGetter level, BlockPos pos) {
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
        return ColumnGrowth.grow(this, level, pos, player, this.defaultBlockState(), kin);
    }
}
