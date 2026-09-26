package com.blazelow.dawnoftimeextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.List;

@Mod(DawnOfTimeExtras.MOD_ID)
public class DawnOfTimeExtrasNeoForge {
    public DawnOfTimeExtrasNeoForge(IEventBus modBus) {
        modBus.addListener(this::onRegister);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DawnOfTimeExtrasNeoForgeClient.init(modBus);
        }
    }

    private void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            DawnOfTimeExtras.init();
        }
        if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            DawnOfTimeExtras.init();
            List<Block> allItems = DawnOfTimeExtras.tabItems();
            event.register(Registries.CREATIVE_MODE_TAB,
                    ResourceLocation.fromNamespaceAndPath(DawnOfTimeExtras.MOD_ID, "extra_additions"),
                    () -> {
                        CreativeModeTab tab = CreativeModeTab.builder()
                                .icon(() -> new ItemStack(DawnOfTimeExtras.TAB_ICON))
                                .title(Component.translatable("itemGroup.dawnoftimeextras.extra_additions"))
                                .displayItems((parameters, output) -> allItems.forEach(output::accept))
                                .build();
                        DawnOfTimeExtras.EXTRA_ADDITIONS_TAB = tab;
                        return tab;
                    });
        }
    }
}
