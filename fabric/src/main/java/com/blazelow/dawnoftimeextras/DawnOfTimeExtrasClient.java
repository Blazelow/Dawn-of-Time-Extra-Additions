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

public class DawnOfTimeExtrasClient implements ClientModInitializer {

    private static final int STILL_WATER = 0x3F76E4;

    @Override
    public void onInitializeClient() {
        for (SignSet set : SignSet.ALL) {
            BlockEntityRenderers.register(set.signEntity, SignRenderer::new);
            BlockEntityRenderers.register(set.hangingSignEntity, HangingSignRenderer::new);
        }

        for (Block block : DawnOfTimeExtras.WATER_TINTED) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.translucent());
        }

        for (Block block : DawnOfTimeExtras.CUTOUT) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tint) -> view == null || pos == null
                        ? STILL_WATER : BiomeColors.getAverageWaterColor(view, pos),
                DawnOfTimeExtras.WATER_TINTED.toArray(new Block[0]));
        ColorProviderRegistry.ITEM.register((stack, tint) -> STILL_WATER,
                DawnOfTimeExtras.WATER_TINTED_ICONS.toArray(new Block[0]));

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
