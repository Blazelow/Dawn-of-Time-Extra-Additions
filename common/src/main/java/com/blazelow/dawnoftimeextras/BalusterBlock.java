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

/**
 * A railing that turns corners, matching Dawn Of Time's balusters.
 *
 * <p>The corner rules and the blockstate live in {@link CorneringBlock}; this only supplies the
 * rail's shape and what it corners with. The rail sits on the side the block faces, which is how
 * Dawn Of Time's model is built - its geometry occupies z 8..16 in the unrotated
 * {@code facing=south} variant.
 */
public class BalusterBlock extends CorneringBlock {
    public static final MapCodec<BalusterBlock> CODEC = simpleCodec(BalusterBlock::new);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.SOUTH, Block.box(0, 0, 8, 16, 16, 16));
        SHAPES.put(Direction.NORTH, Block.box(0, 0, 0, 16, 16, 8));
        SHAPES.put(Direction.EAST, Block.box(8, 0, 0, 16, 16, 16));
        SHAPES.put(Direction.WEST, Block.box(0, 0, 0, 8, 16, 16));
    }

    /** Any baluster corners with any other, so mixed-material railings still turn properly. */
    private static final Predicate<BlockState> KIN = state -> state.getBlock() instanceof BalusterBlock;

    public BalusterBlock(BlockBehaviour.Properties properties) {
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
