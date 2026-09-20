package com.blazelow.dawnoftimeextras;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A tap - Dawn Of Time's faucet, in another stone.
 *
 * <p>Theirs extends their water source trickle, which is where the pour, the stream it lets
 * fall, the splash and the filling of a basin beneath all come from. Ours inherits the lot.
 */
public class FaucetBlock extends org.dawnoftime.dawnoftime.block.templates.FaucetBlock {
    public static final MapCodec<FaucetBlock> CODEC = simpleCodec(FaucetBlock::new);

    public FaucetBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
