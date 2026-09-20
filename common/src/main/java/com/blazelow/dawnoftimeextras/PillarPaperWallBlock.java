package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The squared/window/flowery/flat paper wall variants - Dawn Of Time's own
 * {@code PillarPaneBlock}, in another wood. One shared class for all four, the same way Dawn
 * Of Time's own registry instantiates the identical class for each of its own four variants
 * (confirmed directly, not assumed).
 *
 * <p>Nothing here reimplements it: the multipart connecting geometry and the corner pillar are
 * both theirs. The "flat" variant's connected-texture illusion is resource-pack-only (its own
 * `.png.mcmeta`/Fusion overrides) - Dawn Of Time registers it with this exact same Java class,
 * not a CTM-specific one, confirmed directly rather than assumed from its different-looking
 * texture handling.
 */
public class PillarPaperWallBlock extends org.dawnoftime.dawnoftime.block.templates.PillarPaneBlock {
    public static final MapCodec<PillarPaperWallBlock> CODEC = simpleCodec(PillarPaperWallBlock::new);

    public PillarPaperWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends IronBarsBlock> codec() {
        return CODEC;
    }
}
