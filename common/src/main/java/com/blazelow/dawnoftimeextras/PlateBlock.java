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
 * Half a block deep and full height, matching Dawn Of Time's plate - a vertical slab that turns
 * corners.
 *
 * <p>The corner rules and the blockstate come from {@link CorneringBlock}; this only supplies the
 * mass and what it corners with. Their model occupies z 8..16 in the unrotated
 * {@code facing=south} variant, so the plate stands on the side the block faces.
 */
public class PlateBlock extends CorneringBlock {
    public static final MapCodec<PlateBlock> CODEC = simpleCodec(PlateBlock::new);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.SOUTH, Block.box(0, 0, 8, 16, 16, 16));
        SHAPES.put(Direction.NORTH, Block.box(0, 0, 0, 16, 16, 8));
        SHAPES.put(Direction.EAST, Block.box(8, 0, 0, 16, 16, 16));
        SHAPES.put(Direction.WEST, Block.box(0, 0, 0, 8, 16, 16));
    }

    /** Any plate corners with any other, so a run can change material and still turn. */
    private static final Predicate<BlockState> KIN = state -> state.getBlock() instanceof PlateBlock;

    public PlateBlock(BlockBehaviour.Properties properties) {
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
