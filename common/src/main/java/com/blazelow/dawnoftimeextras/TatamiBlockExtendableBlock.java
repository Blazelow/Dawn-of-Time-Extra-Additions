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

/**
 * A genuine full-height tatami block that connects independently on all four horizontal
 * sides - not a single growth axis like the old thin mat (removed 2026-09-14; it only ever
 * ran in one line). A full block naturally sits in a 2D grid (a floor, not a
 * strip), so each of {@link #NORTH}/{@link #SOUTH}/{@link #EAST}/{@link #WEST} tracks whether
 * an unrotated neighbour of this same block touches that specific side, and every one of the
 * six faces (the four sides plus top and bottom) opens on exactly the sides that actually
 * touch a neighbour - a 3x3 patch of these reads as one seamless slab in every direction, not
 * just along one line.
 *
 * <p>{@link #FACING} (added 2026-09-14) records the horizontal direction the player was
 * facing at placement - it never changes which real-world neighbours this block detects
 * (that stays exactly {@link #NORTH}/{@link #SOUTH}/{@link #EAST}/{@link #WEST}, absolute and
 * unaffected by facing), it only changes which textures and models are picked, by reading those absolute
 * booleans in the block's own local coordinate system. No blockstate `y` rotation is used anywhere; each of the 64
 * (facing x north x south x east x west) states points at its own explicit model.
 *
 * <p>Renamed from {@code SmallTatamiBlockExtendableBlock} (registered id
 * {@code small_tatami_block_extendable} -> {@code tatami_block_extendable}) on request,
 * 2026-09-14 - a pure rename, no behaviour change.
 */
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
        // Same reliability fix as the thin mat: getStateForPlacement's own computation is a
        // snapshot taken at click time, and updateShape's own neighbour-notification cascade
        // alone isn't a reliable way to make sure every neighbour that now touches this block
        // gets its own matching side refreshed too. Force a fresh, authoritative recheck of
        // this block and everything within two tiles of it, not just the immediate neighbours -
        // reported in-game on a 4-in-a-row: only the middle seam ever connected, the two outer
        // ones never did, even though a one-hop refresh (this block plus its direct neighbours)
        // should have reached every block in a row that short. Widening the refresh radius
        // rather than continuing to guess at the exact gap in the one-hop version.
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

    /** This position plus every position reachable within two horizontal steps - covers a
     *  block placed at either end of an already-3-long row, not just its immediate neighbour. */
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
