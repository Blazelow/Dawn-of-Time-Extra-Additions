package com.blazelow.dawnoftimeextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

@Mod(DawnOfTimeExtras.MOD_ID)
public class DawnOfTimeExtrasForge {
    public DawnOfTimeExtrasForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onRegister);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DawnOfTimeExtrasForgeClient.init(modBus);
        }
    }

    private void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            DawnOfTimeExtras.init();
            DawnOfTimeExtras.BLOCKS.forEach((id, block) -> event.register(Registries.BLOCK, id, () -> block));
        }
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            DawnOfTimeExtras.init();
            DawnOfTimeExtras.ITEMS.forEach((id, item) -> event.register(Registries.ITEM, id, () -> item));
        }
        if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            DawnOfTimeExtras.init();
            DawnOfTimeExtras.BLOCK_ENTITIES.forEach((id, type) -> event.register(Registries.BLOCK_ENTITY_TYPE, id, () -> type));
        }
        if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            DawnOfTimeExtras.init();
            List<ItemLike> allItems = DawnOfTimeExtras.tabItems();
            event.register(Registries.CREATIVE_MODE_TAB,
                    new ResourceLocation(DawnOfTimeExtras.MOD_ID, "extra_additions"),
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
