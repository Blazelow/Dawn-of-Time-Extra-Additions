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

public class DoubleFutonBlock extends BedBlock {

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

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

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

                    level.setBlock(part, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, part, Block.getId(other));
                }
            }

            if (!player.isCreative()) {
                popResource(level, pos, new ItemStack(this));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

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
