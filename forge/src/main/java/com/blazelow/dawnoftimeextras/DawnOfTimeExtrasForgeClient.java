package com.blazelow.dawnoftimeextras;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

public final class DawnOfTimeExtrasForgeClient {

    private static final int STILL_WATER = 0x3F76E4;

    private DawnOfTimeExtrasForgeClient() {
    }

    static void init(IEventBus modBus) {
        modBus.addListener(DawnOfTimeExtrasForgeClient::onRenderers);
        modBus.addListener(DawnOfTimeExtrasForgeClient::onBlockColours);
        modBus.addListener(DawnOfTimeExtrasForgeClient::onItemColours);
        modBus.addListener(DawnOfTimeExtrasForgeClient::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(DawnOfTimeExtrasForgeClient::onTooltip);
    }

    private static void onRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (SignSet set : SignSet.ALL) {
            event.registerBlockEntityRenderer(set.signEntity, SignRenderer::new);
            event.registerBlockEntityRenderer(set.hangingSignEntity, HangingSignRenderer::new);
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (SignSet set : SignSet.ALL) {
                Sheets.addWoodType(set.woodType);
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
