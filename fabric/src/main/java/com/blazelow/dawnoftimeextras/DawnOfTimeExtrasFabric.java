package com.blazelow.dawnoftimeextras;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import net.minecraft.world.level.block.Block;

public class DawnOfTimeExtrasFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DawnOfTimeExtras.init();
        List<Block> allItems = DawnOfTimeExtras.tabItems();

        DawnOfTimeExtras.EXTRA_ADDITIONS_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(DawnOfTimeExtras.MOD_ID, "extra_additions"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(DawnOfTimeExtras.TAB_ICON))
                        .title(Component.translatable("itemGroup.dawnoftimeextras.extra_additions"))
                        .displayItems((parameters, output) -> allItems.forEach(output::accept))
                        .build());
    }
}
