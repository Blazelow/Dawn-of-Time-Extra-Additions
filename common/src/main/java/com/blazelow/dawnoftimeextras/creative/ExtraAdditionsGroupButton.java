package com.blazelow.dawnoftimeextras.creative;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExtraAdditionsGroupButton extends Button {
    private final ResourceLocation iconResource;
    private final int iconU;
    private final int iconV;

    public ExtraAdditionsGroupButton(int x, int y, Component message, Button.OnPress pressable,
                                     ResourceLocation iconResource, int iconU, int iconV) {
        super(x, y, 20, 20, message, pressable, Button.DEFAULT_NARRATION);
        this.iconResource = iconResource;
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY()
                    && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            graphics.pose().pushPose();
            RenderSystem.clearColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            int state = !this.active ? 0 : this.isHoveredOrFocused() ? 2 : 1;
            graphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                    20, 4, 200, 20, 0, 46 + state * 20);
            RenderSystem.disableBlend();
            graphics.pose().popPose();
            graphics.pose().pushPose();
            if (!this.active) {
                graphics.setColor(0.5F, 0.5F, 0.5F, 1.0F);
            }
            RenderSystem.enableBlend();
            graphics.blit(this.iconResource, this.getX() + 2, this.getY() + 2, this.iconU, this.iconV, 16, 16);
            RenderSystem.disableBlend();
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.pose().popPose();
        }
    }
}
