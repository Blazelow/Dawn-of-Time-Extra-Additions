package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The boards' own slab - Dawn Of Time's own {@code spruce_boards_slab}, in another wood (and,
 * for charred_spruce, a piece they never made at all - see {@link BoardsStairsBlock}).
 *
 * <p>A bare {@code SlabBlockDoT} with fire spread/destruction speeds set after construction,
 * matching Dawn Of Time's own choice for this exact block.
 */
public class BoardsSlabBlock extends org.dawnoftime.dawnoftime.block.templates.SlabBlockDoT {
    public static final MapCodec<BoardsSlabBlock> CODEC = simpleCodec(BoardsSlabBlock::new);

    public BoardsSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable(2, 3);
    }

    @Override
    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }
}
