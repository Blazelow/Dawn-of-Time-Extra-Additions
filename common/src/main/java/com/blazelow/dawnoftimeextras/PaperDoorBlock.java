package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

/**
 * A door centred in the middle of the block rather than at one edge - Dawn Of Time's own
 * spruce paper door, in another wood.
 *
 * <p>Nothing here reimplements it: the centred hitbox/model split (not a normal door's full
 * one-side swing), the open/hinge/powered state, and the block set (governs sound and whether
 * it opens by hand) are all theirs, because this extends their block rather than copying it.
 * Dawn Of Time's own spruce door uses {@link BlockSetType#BAMBOO} regardless of which wood the
 * door is framed in, so this does too - it is not the frame material's own set.
 */
public class PaperDoorBlock extends org.dawnoftime.dawnoftime.block.templates.CenteredDoorBlock {
    public static final MapCodec<PaperDoorBlock> CODEC = simpleCodec(PaperDoorBlock::new);

    public PaperDoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.BAMBOO);
    }

    @Override
    public MapCodec<? extends DoorBlock> codec() {
        return CODEC;
    }
}
