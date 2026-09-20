package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The parquet-style "boards" block - Dawn Of Time's own {@code spruce_boards}, in another wood.
 *
 * <p>A bare {@code RotatedPillarBlockDoT} (vanilla's axis-rotatable pillar behaviour plus Dawn
 * Of Time's fire tracking), matching their own class for this exact block - not
 * {@code BlockDoT}, which is what their {@code charred_spruce_boards} uses instead. The two
 * designs differ on Dawn Of Time's own side (one rotates like a log, the other doesn't); this
 * wrapper only covers the rotating one, since charred_spruce_boards already exists natively and
 * isn't being duplicated here.
 */
public class BoardsBlock extends org.dawnoftime.dawnoftime.block.templates.RotatedPillarBlockDoT {
    public static final MapCodec<BoardsBlock> CODEC = simpleCodec(BoardsBlock::new);

    public BoardsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBurnable();
    }

    @Override
    public MapCodec<? extends RotatedPillarBlock> codec() {
        return CODEC;
    }
}
