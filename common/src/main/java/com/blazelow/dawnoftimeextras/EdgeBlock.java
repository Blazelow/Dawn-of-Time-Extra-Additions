package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public class EdgeBlock extends CorneringBlock {
    public static final MapCodec<EdgeBlock> CODEC = simpleCodec(EdgeBlock::new);

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    private static final Map<Direction, VoxelShape> BOTTOM = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> TOP = new EnumMap<>(Direction.class);

    static {
        BOTTOM.put(Direction.SOUTH, Block.box(0, 0, 8, 16, 8, 16));
        BOTTOM.put(Direction.NORTH, Block.box(0, 0, 0, 16, 8, 8));
        BOTTOM.put(Direction.EAST, Block.box(8, 0, 0, 16, 8, 16));
        BOTTOM.put(Direction.WEST, Block.box(0, 0, 0, 8, 8, 16));
        TOP.put(Direction.SOUTH, Block.box(0, 8, 8, 16, 16, 16));
        TOP.put(Direction.NORTH, Block.box(0, 8, 0, 16, 16, 8));
        TOP.put(Direction.EAST, Block.box(8, 8, 0, 16, 16, 16));
        TOP.put(Direction.WEST, Block.box(0, 8, 0, 8, 16, 16));
    }

    private static final Predicate<BlockState> KIN = state -> state.getBlock() instanceof EdgeBlock;

    public EdgeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, Half.BOTTOM));
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Direction face = context.getClickedFace();

        boolean top = face == Direction.DOWN
                || (face != Direction.UP
                    && context.getClickLocation().y - context.getClickedPos().getY() > 0.5D);
        return state.setValue(HALF, top ? Half.TOP : Half.BOTTOM);
    }

    @Override
    protected VoxelShape sideShape(BlockState state, Direction side) {
        return (state.getValue(HALF) == Half.TOP ? TOP : BOTTOM).get(side);
    }

    @Override
    protected Predicate<BlockState> kin() {
        return KIN;
    }
}
