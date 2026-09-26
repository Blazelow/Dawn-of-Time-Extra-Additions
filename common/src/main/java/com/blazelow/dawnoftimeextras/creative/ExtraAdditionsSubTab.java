package com.blazelow.dawnoftimeextras.creative;

import com.blazelow.dawnoftimeextras.ExtraAdditionsCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;

public enum ExtraAdditionsSubTab {
    BUILDING("building"),
    FURNITURE("furniture"),
    PLASTERED("plastered"),
    PAINTED("painted"),
    PUUC("puuc");

    private static final String[] FURNITURE_WORDS = {
            "lantern", "lamp", "screen", "flag", "futon", "cushion", "chair", "table", "fireplace", "irori",
            "candlestick", "chandelier", "teapot", "teacup", "pot", "sake"};

    public final ResourceLocation on;
    public final ResourceLocation off;
    public final Component tooltip;

    ExtraAdditionsSubTab(String name) {
        this.on = ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder", "textures/gui/subtab_" + name + "_on.png");
        this.off = ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder", "textures/gui/subtab_" + name + "_off.png");
        this.tooltip = Component.translatable("tooltip.dawnoftimebuilder.subtab." + name);
    }

    private static boolean isFurniture(String path) {
        if (path.contains("fancy_lantern")) {
            return false;
        }
        if (path.contains("tatami") && !path.contains("block")) {
            return true;
        }
        for (String word : FURNITURE_WORDS) {
            if (path.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Block block) {
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        boolean puuc = path.contains("puuc");
        boolean painted = !puuc && (path.contains("painted") || path.endsWith("_template"));
        return switch (this) {
            case BUILDING -> !isFurniture(path);
            case FURNITURE -> isFurniture(path);
            case PUUC -> puuc;
            case PAINTED -> painted;
            case PLASTERED -> !puuc && !painted && path.contains("plastered");
        };
    }

    public static List<ExtraAdditionsSubTab> forCategory(ExtraAdditionsCategory category) {
        return category == ExtraAdditionsCategory.PRE_COLOMBIAN
                ? List.of(PLASTERED, PAINTED, PUUC) : List.of(BUILDING, FURNITURE);
    }
}
