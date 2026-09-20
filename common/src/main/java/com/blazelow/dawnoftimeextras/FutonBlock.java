package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * An ordinary 1x2 futon, in the colours Dawn Of Time does not ship.
 *
 * <p>Plain vanilla bed behaviour - their own futon is a {@code BedBlock} too - with the two
 * changes that a custom-modelled bed needs: the render shape forced back to {@code MODEL}, or
 * the vanilla bed renderer draws a bed on top of the futon, and no block entity, because the
 * one {@link BedBlock} builds is bound to {@code BlockEntityType.BED} whose valid-block list
 * cannot be added to. It only carries the dyed colour for that renderer, which this bypasses.
 */
public class FutonBlock extends BedBlock {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 4, 16);

    public FutonBlock(BlockBehaviour.Properties properties) {
        super(DyeColor.LIGHT_GRAY, properties);
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
}
