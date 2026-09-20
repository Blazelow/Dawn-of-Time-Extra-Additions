package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

/**
 * The boards' own stairs - Dawn Of Time's own {@code spruce_boards_stairs}, in another wood
 * (and, for charred_spruce, a piece they never made at all - Dawn Of Time only ever gave
 * {@code charred_spruce_boards} the plain block, none of stairs/slab/edge).
 *
 * <p>Extends their own {@code StairsBlockDoT} rather than vanilla's {@code StairBlock} directly
 * so fire spread/destruction tracking works the same way every other flammable piece in this
 * mod gets it - unlike the masonry family's plain-stone stairs, boards genuinely burn. Its base
 * block supplier is per-wood (unlike {@link RoofSupportBlock}'s fixed tile-slab reference), so
 * it can't be baked into a single shared static codec the same way - each instance builds its
 * own {@link MapCodec}, closing over the same supplier it was built with.
 */
public class BoardsStairsBlock extends org.dawnoftime.dawnoftime.block.templates.StairsBlockDoT {
    private final MapCodec<BoardsStairsBlock> codec;

    public BoardsStairsBlock(Supplier<Block> baseBlock, BlockBehaviour.Properties properties) {
        super(baseBlock, properties);
        this.codec = simpleCodec(p -> new BoardsStairsBlock(baseBlock, p));
    }

    @Override
    public MapCodec<? extends StairBlock> codec() {
        return this.codec;
    }
}
