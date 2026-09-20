package com.blazelow.dawnoftimeextras;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

/**
 * Grow-by-hand behaviour for stacking blocks, matching Dawn Of Time's columns.
 *
 * <p>Taken from how theirs actually behaves: right-clicking adds a section on top of the stack
 * but only while you are holding another of the same block, which is what stops it fighting with
 * ordinary block placement; sneaking and right-clicking takes the top section off again. Both
 * respect creative mode - a section is only consumed, or given back, in survival.
 */
final class ColumnGrowth {

    private ColumnGrowth() {
    }

    /** The highest block of the stack this one belongs to. */
    static BlockPos topOf(Level level, BlockPos pos, Predicate<BlockState> kin) {
        BlockPos top = pos;
        while (kin.test(level.getBlockState(top.above()))) {
            top = top.above();
        }
        return top;
    }

    /** Adds {@code toPlace} above the stack, if the player is holding one and there is room. */
    static InteractionResult grow(Block block, Level level, BlockPos pos, Player player,
                                  BlockState toPlace, Predicate<BlockState> kin) {
        ItemStack held = player.getItemInHand(player.getUsedItemHand());
        if (held.isEmpty() || held.getItem() != block.asItem()) {
            return InteractionResult.PASS;
        }
        BlockPos above = topOf(level, pos, kin).above();
        if (above.getY() >= level.getMaxBuildHeight() || !level.getBlockState(above).isAir()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(above, toPlace, Block.UPDATE_ALL);
        }
        if (!player.isCreative()) {
            held.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Takes the top section off the stack and drops it. {@code stacked} guards a lone block. */
    static InteractionResult shrink(Level level, BlockPos pos, Player player,
                                    boolean stacked, Predicate<BlockState> kin) {
        if (!stacked) {
            return InteractionResult.PASS;
        }
        BlockPos top = topOf(level, pos, kin);
        if (!level.isClientSide) {
            BlockState removed = level.getBlockState(top);
            level.setBlock(top, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            if (!player.isCreative()) {
                Block.dropResources(removed, level, top, null, player, ItemStack.EMPTY);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
