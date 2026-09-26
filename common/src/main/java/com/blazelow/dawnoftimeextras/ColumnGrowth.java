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

final class ColumnGrowth {

    private ColumnGrowth() {
    }

    static BlockPos topOf(Level level, BlockPos pos, Predicate<BlockState> kin) {
        BlockPos top = pos;
        while (kin.test(level.getBlockState(top.above()))) {
            top = top.above();
        }
        return top;
    }

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
