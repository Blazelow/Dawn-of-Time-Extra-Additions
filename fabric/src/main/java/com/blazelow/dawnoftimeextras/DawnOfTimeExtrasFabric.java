package com.blazelow.dawnoftimeextras;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DawnOfTimeExtrasFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DawnOfTimeExtras.init();
        DawnOfTimeExtras.BLOCKS.forEach((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
        DawnOfTimeExtras.ITEMS.forEach((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
        DawnOfTimeExtras.BLOCK_ENTITIES.forEach((id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));
        List<Block> allItems = DawnOfTimeExtras.tabItems();

        DawnOfTimeExtras.EXTRA_ADDITIONS_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(DawnOfTimeExtras.MOD_ID, "extra_additions"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(DawnOfTimeExtras.TAB_ICON))
                        .title(Component.translatable("itemGroup.dawnoftimeextras.extra_additions"))
                        .displayItems((parameters, output) -> allItems.forEach(output::accept))
                        .build());
    }
}
