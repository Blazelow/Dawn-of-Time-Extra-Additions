package com.blazelow.dawnoftimeextras.creative;

import com.blazelow.dawnoftimeextras.ExtraAdditionsCategory;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Reproduction of Dawn Of Time's {@code CategoryButton} (decompiled): 32x28, extends {@link Button},
 * frame blitted from their {@code gui/creative_icons.png} at (x-1, y) size 31x28 with v = 0 selected /
 * 28 unselected, logo at (x + 6 or 9, y + 6) 16x16 from their {@code textures/item/logo_<name>.png}.
 * Category id = page * 4 + slot index; slots past the last category are inactive and draw nothing.
 */
public class ExtraAdditionsCategoryButton extends Button {
    public static final ResourceLocation CREATIVE_ICONS =
            ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder", "textures/gui/creative_icons.png");

    private static final ResourceLocation[] BUTTON_ICONS = fillButtonIcons();
    private static final Component[] BUTTON_TOOLTIPS = fillButtonTooltips();

    private final IntSupplier page;
    private final int index;
    private boolean selected = false;

    public ExtraAdditionsCategoryButton(int x, int y, int index, Consumer<ExtraAdditionsCategoryButton> onPress,
                                        IntSupplier page) {
        super(x, y, 32, 28, Component.empty(),
                button -> onPress.accept((ExtraAdditionsCategoryButton) button), Button.DEFAULT_NARRATION);
        this.index = index;
        this.page = page;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public int getCategoryID() {
        return this.page.getAsInt() * 4 + this.index;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.active) {
            return;
        }
        graphics.pose().pushPose();
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        graphics.blit(CREATIVE_ICONS, this.getX() - 1, this.getY(), 0, this.selected ? 0 : 28, 31, 28);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
        graphics.pose().pushPose();
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        graphics.blit(BUTTON_ICONS[this.getCategoryID()], this.getX() + (this.selected ? 6 : 9), this.getY() + 6,
                0, 0.0F, 0.0F, 16, 16, 16, 16);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
        if (this.isHovered) {
            graphics.renderTooltip(Minecraft.getInstance().font, BUTTON_TOOLTIPS[this.getCategoryID()], mouseX, mouseY);
        }
    }

    private static ResourceLocation[] fillButtonIcons() {
        ExtraAdditionsCategory[] values = ExtraAdditionsCategory.values();
        ResourceLocation[] table = new ResourceLocation[values.length];
        for (int i = 0; i < values.length; i++) {
            table[i] = ResourceLocation.fromNamespaceAndPath("dawnoftimebuilder",
                    "textures/item/logo_" + values[i].dotName + ".png");
        }
        return table;
    }

    private static Component[] fillButtonTooltips() {
        ExtraAdditionsCategory[] values = ExtraAdditionsCategory.values();
        Component[] tooltips = new Component[values.length];
        for (int i = 0; i < values.length; i++) {
            tooltips[i] = Component.translatable("gui.dawnoftimebuilder." + values[i].dotName);
        }
        return tooltips;
    }
}
