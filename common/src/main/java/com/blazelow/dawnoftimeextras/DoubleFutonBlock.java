package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * A futon for two, laid out 2x2.
 *
 * <p>Minecraft allows exactly one sleeper per bed, so this is really <em>two</em> beds that
 * happen to share one item and one look. Each half is an ordinary 1x2 vanilla bed running
 * foot-to-head, and the two sit side by side; vanilla's own bed machinery pairs each half's
 * head and foot along the facing axis and never looks sideways, so the halves stay completely
 * independent and two players can turn in at once, one per side.
 *
 * <p>All four blocks are placed by one item and taken out together, so it behaves as a single
 * piece of furniture. The render shape is forced back to {@code MODEL} - Dawn Of Time's futon
 * does the same - or the vanilla bed renderer would draw a bed on top of the futon model.
 */
public class DoubleFutonBlock extends BedBlock {
    // No codec() override: BedBlock declares it as a concrete MapCodec<BedBlock>, which a
    // subclass cannot narrow. Inheriting it is harmless - the codec only serialises the block
    // definition, which nothing here relies on.

    public static final EnumProperty<FutonSide> SIDE = EnumProperty.create("side", FutonSide.class);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

    public DoubleFutonBlock(BlockBehaviour.Properties properties) {
        super(DyeColor.LIGHT_GRAY, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, BedPart.FOOT)
                .setValue(OCCUPIED, false)
                .setValue(SIDE, FutonSide.LEFT));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SIDE);
    }

    /** Without this the vanilla bed renderer draws over the futon model. */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /**
     * No block entity, unlike a real bed.
     *
     * <p>{@link BedBlock} builds a {@link net.minecraft.world.level.block.entity.BedBlockEntity},
     * which is bound to {@code BlockEntityType.BED} - and that type's list of valid blocks is
     * closed, so placing this threw {@code IllegalStateException: Invalid block entity
     * minecraft:bed} the instant the block went down. The entity exists only to carry the dyed
     * colour for the vanilla bed renderer, which {@link #getRenderShape} already bypasses, and
     * nothing else reads it: sleeping, occupancy and spawn-setting are all blockstate-driven.
     */
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockState state = this.defaultBlockState().setValue(FACING, facing);
        // the clicked block is the left foot; the other three have to be free as well
        for (BlockPos pos : parts(state, context.getClickedPos())) {
            if (pos.equals(context.getClickedPos())) {
                continue;
            }
            if (!context.getLevel().getBlockState(pos).canBeReplaced(context)
                    || !context.getLevel().getWorldBorder().isWithinBounds(pos)) {
                return null;
            }
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.isClientSide) {
            return;
        }
        Direction facing = state.getValue(FACING);
        BlockPos right = pos.relative(facing.getClockWise());
        // the item places the left foot; fill in the other three
        level.setBlock(pos.relative(facing), state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
        level.setBlock(right, state.setValue(SIDE, FutonSide.RIGHT), Block.UPDATE_ALL);
        level.setBlock(right.relative(facing),
                state.setValue(SIDE, FutonSide.RIGHT).setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
        level.blockUpdated(pos, Blocks.AIR);
        state.updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            for (BlockPos part : parts(state, pos)) {
                if (part.equals(pos)) {
                    continue;
                }
                BlockState other = level.getBlockState(part);
                if (other.is(this)) {
                    // 35 = update neighbours but suppress drops, the flag vanilla beds use
                    level.setBlock(part, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, part, Block.getId(other));
                }
            }
            // the loot table is empty on purpose: one item back for the whole futon, no matter
            // which of the four blocks was hit
            if (!player.isCreative()) {
                popResource(level, pos, new ItemStack(this));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    /** All four positions of the futon this block belongs to: left foot, left head, right foot, right head. */
    private BlockPos[] parts(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos foot = state.getValue(PART) == BedPart.HEAD ? pos.relative(facing.getOpposite()) : pos;
        BlockPos leftFoot = state.getValue(SIDE) == FutonSide.RIGHT
                ? foot.relative(facing.getClockWise().getOpposite())
                : foot;
        BlockPos rightFoot = leftFoot.relative(facing.getClockWise());
        return new BlockPos[]{leftFoot, leftFoot.relative(facing), rightFoot, rightFoot.relative(facing)};
    }
}
