package com.blazelow.dawnoftimeextras;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.Map;

/**
 * Client-side wiring on NeoForge: the same four jobs as the Fabric client class - sign
 * renderers, render layers, the fountain water colour and the extra tooltip lines.
 */
public final class DawnOfTimeExtrasNeoForgeClient {
    /** Vanilla's water colour, for the item icons - an item has no biome to ask. */
    private static final int STILL_WATER = 0x3F76E4;

    private DawnOfTimeExtrasNeoForgeClient() {
    }

    static void init(IEventBus modBus) {
        modBus.addListener(DawnOfTimeExtrasNeoForgeClient::onRenderers);
        modBus.addListener(DawnOfTimeExtrasNeoForgeClient::onBlockColours);
        modBus.addListener(DawnOfTimeExtrasNeoForgeClient::onItemColours);
        modBus.addListener(DawnOfTimeExtrasNeoForgeClient::onClientSetup);
        NeoForge.EVENT_BUS.addListener(DawnOfTimeExtrasNeoForgeClient::onTooltip);
    }

    private static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (SignSet set : SignSet.ALL) {
            event.registerBlockEntityRenderer(set.signEntity, SignRenderer::new);
            event.registerBlockEntityRenderer(set.hangingSignEntity, HangingSignRenderer::new);
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        // Most models already carry "render_type" (a NeoForge extension), but the ones copied
        // from a model without it need the layer set here, exactly as the Fabric client does.
        event.enqueueWork(() -> {
            for (Block block : DawnOfTimeExtras.WATER_TINTED) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
            }
            for (Block block : DawnOfTimeExtras.CUTOUT) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            }
        });
    }

    private static void onBlockColours(RegisterColorHandlersEvent.Block event) {
        event.register((state, view, pos, tint) -> view == null || pos == null
                        ? STILL_WATER : BiomeColors.getAverageWaterColor(view, pos),
                DawnOfTimeExtras.WATER_TINTED.toArray(new Block[0]));
    }

    private static void onItemColours(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> STILL_WATER,
                DawnOfTimeExtras.WATER_TINTED_ICONS.toArray(new Block[0]));
    }

    private static void onTooltip(ItemTooltipEvent event) {
        for (Map.Entry<Block, String[]> note : DawnOfTimeExtras.TOOLTIPS.entrySet()) {
            if (event.getItemStack().is(note.getKey().asItem())) {
                for (String key : note.getValue()) {
                    event.getToolTip().add(Component.translatable(key));
                }
                return;
            }
        }
    }
}
