package com.blazelow.dawnoftimeextras;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.Block;

import java.util.Map;

/**
 * Client-side wiring: sign renderers, and the colour the fountain's water is drawn in.
 *
 * <p>The signs need only their renderers attached to our own block entity types. Nothing else
 * has to be wired up by hand: vanilla builds a sign model layer and a texture material for every
 * registered wood type, and the signs atlas takes a whole directory across every namespace, so
 * our entity textures are stitched in on their own.
 */
public class DawnOfTimeExtrasClient implements ClientModInitializer {

    /** Vanilla's water colour, for the item icons - an item has no biome to ask. */
    private static final int STILL_WATER = 0x3F76E4;

    @Override
    public void onInitializeClient() {
        for (SignSet set : SignSet.ALL) {
            BlockEntityRenderers.register(set.signEntity, SignRenderer::new);
            BlockEntityRenderers.register(set.hangingSignEntity, HangingSignRenderer::new);
        }

        // The pool surfaces and the jet's spray are drawn on tint index 0, the same as vanilla
        // water, so they take the biome's water colour and match the sea they are built beside.
        // Water has to be drawn on the translucent layer or it comes out flat and opaque.
        // The render_type in the model files is a NeoForge extension that Fabric never reads,
        // so the layer is set here instead - which is exactly what Dawn Of Time does for their
        // pool, small pool, jet, faucet and trickle.
        for (Block block : DawnOfTimeExtras.WATER_TINTED) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
        }
        // And the layer Dawn Of Time gives each block the rest of ours are drawn from. Without
        // it a hearth's fire, the gaps in an iron fence and a mat's woven edge are filled in
        // solid black, because solid is what a block renders on when nobody says otherwise.
        for (Block block : DawnOfTimeExtras.CUTOUT) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tint) -> view == null || pos == null
                        ? STILL_WATER : BiomeColors.getAverageWaterColor(view, pos),
                DawnOfTimeExtras.WATER_TINTED.toArray(new Block[0]));
        ColorProviderRegistry.ITEM.register((stack, tint) -> STILL_WATER,
                DawnOfTimeExtras.WATER_TINTED_ICONS.toArray(new Block[0]));

        // A tooltip callback rather than an appendHoverText override on each block: the blocks
        // that want a note are several different vanilla classes, and this needs no subclass of
        // any of them.
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            for (Map.Entry<Block, String[]> note : DawnOfTimeExtras.TOOLTIPS.entrySet()) {
                if (stack.is(note.getKey().asItem())) {
                    for (String key : note.getValue()) {
                        lines.add(Component.translatable(key));
                    }
                    return;
                }
            }
        });
    }
}
