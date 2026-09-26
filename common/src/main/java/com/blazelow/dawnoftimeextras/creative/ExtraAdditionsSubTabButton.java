package com.blazelow.dawnoftimeextras.creative;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/**
 * A 12x12 set button, drawn like Dawn Of Time's own sub-tab button: the "on" icon when selected, darker on hover.
 * One of a fixed few slots: which set it stands for changes with the page (see {@link #setSubTab}).
 */
public class ExtraAdditionsSubTabButton extends Button {
    private ExtraAdditionsSubTab subTab;
    private boolean selected = false;

    public ExtraAdditionsSubTabButton(int x, int y, ExtraAdditionsSubTab subTab, Button.OnPress onPress) {
        super(x, y, 12, 12, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.setSubTab(subTab);
    }

    public void setSubTab(ExtraAdditionsSubTab subTab) {
        this.subTab = subTab;
        this.setTooltip(Tooltip.create(subTab.tooltip));
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.pose().pushPose();
        if (this.isHoveredOrFocused() && this.active) {
            graphics.setColor(0.7F, 0.7F, 0.7F, 1.0F);
        }
        RenderSystem.enableBlend();
        graphics.blit(this.selected ? this.subTab.on : this.subTab.off, this.getX() - 1, this.getY(), 0,
                0.0F, 0.0F, 12, 12, 12, 12);
        RenderSystem.disableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.pose().popPose();
    }
}
