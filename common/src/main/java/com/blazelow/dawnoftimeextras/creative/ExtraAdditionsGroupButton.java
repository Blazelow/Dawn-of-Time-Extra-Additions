package com.blazelow.dawnoftimeextras.creative;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Reproduction of Dawn Of Time's {@code GroupButton} (decompiled): the 20x20 page-scroll arrows. Vanilla
 * button sprite drawn twice (as theirs does), then a 16x16 icon at (x+2, y+2) from {@code creative_icons.png}.
 */
public class ExtraAdditionsGroupButton extends Button {
    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

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
            graphics.blitSprite(BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()),
                    this.getX(), this.getY(), this.getWidth(), this.getHeight());
            graphics.blitSprite(BUTTON_SPRITES.get(this.active, this.isHoveredOrFocused()),
                    this.getX(), this.getY(), this.getWidth(), this.getHeight());
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
