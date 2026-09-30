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
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DawnOfTimeExtrasClient implements ClientModInitializer {

    private static final int STILL_WATER = 0x3F76E4;

    private static Set<ResourceLocation> waterBlockIds;

    private static Set<ResourceLocation> waterBlocks() {
        if (waterBlockIds == null) {
            waterBlockIds = new HashSet<>();
            for (Block block : DawnOfTimeExtras.WATER_TINTED) {
                waterBlockIds.add(BuiltInRegistries.BLOCK.getKey(block));
            }
        }
        return waterBlockIds;
    }

    @Override
    public void onInitializeClient() {
        for (SignSet set : SignSet.ALL) {
            BlockEntityRenderers.register(set.signEntity, SignRenderer::new);
            BlockEntityRenderers.register(set.hangingSignEntity, HangingSignRenderer::new);
        }

        for (Block block : DawnOfTimeExtras.WATER_TINTED) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        for (Block block : DawnOfTimeExtras.CUTOUT) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        ModelLoadingPlugin.register(pluginContext -> pluginContext.modifyModelAfterBake().register((model, context) -> {
            if (model == null || !(context.id() instanceof ModelResourceLocation id) || id.getVariant().equals("inventory")) {
                return model;
            }
            if (waterBlocks().contains(new ResourceLocation(id.getNamespace(), id.getPath()))) {
                return new WaterQuadModel(model);
            }
            return model;
        }));

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tint) -> view == null || pos == null
                        ? STILL_WATER : BiomeColors.getAverageWaterColor(view, pos),
                DawnOfTimeExtras.WATER_TINTED.toArray(new Block[0]));
        ColorProviderRegistry.ITEM.register((stack, tint) -> STILL_WATER,
                DawnOfTimeExtras.WATER_TINTED_ICONS.toArray(new Block[0]));

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
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
