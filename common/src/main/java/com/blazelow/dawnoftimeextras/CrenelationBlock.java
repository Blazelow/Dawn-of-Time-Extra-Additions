package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public class CrenelationBlock extends CorneringBlock {
    public static final MapCodec<CrenelationBlock> CODEC = simpleCodec(CrenelationBlock::new);

    private static final double HEIGHT = 14.657;

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.SOUTH, Block.box(0, 0, 8, 16, HEIGHT, 16));
        SHAPES.put(Direction.NORTH, Block.box(0, 0, 0, 16, HEIGHT, 8));
        SHAPES.put(Direction.EAST, Block.box(8, 0, 0, 16, HEIGHT, 16));
        SHAPES.put(Direction.WEST, Block.box(0, 0, 0, 8, HEIGHT, 16));
    }

    private static final Predicate<BlockState> KIN = state -> state.getBlock() instanceof CrenelationBlock;

    public CrenelationBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape sideShape(BlockState state, Direction side) {
        return SHAPES.get(side);
    }

    @Override
    protected Predicate<BlockState> kin() {
        return KIN;
    }
}
