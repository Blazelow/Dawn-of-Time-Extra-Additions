package com.blazelow.dawnoftimeextras.mixin;

import com.blazelow.dawnoftimeextras.DawnOfTimeExtras;
import com.blazelow.dawnoftimeextras.ExtraAdditionsCategory;
import com.blazelow.dawnoftimeextras.creative.ExtraAdditionsCategoryButton;
import com.blazelow.dawnoftimeextras.creative.ExtraAdditionsGroupButton;
import com.blazelow.dawnoftimeextras.creative.ExtraAdditionsSubTab;
import com.blazelow.dawnoftimeextras.creative.ExtraAdditionsSubTabButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class ExtraAdditionsCreativeMixin
        extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Unique
    private List<ExtraAdditionsCategoryButton> dawnoftimeextras$buttons;
    @Unique
    private List<ExtraAdditionsSubTabButton> dawnoftimeextras$subTabButtons;
    @Unique
    private static int dawnoftimeextras$selectedSubTab = -1;
    @Unique
    private Button dawnoftimeextras$btnScrollUp;
    @Unique
    private Button dawnoftimeextras$btnScrollDown;
    @Unique
    private boolean dawnoftimeextras$tabSelected;
    @Unique
    private static int dawnoftimeextras$selectedCategoryID = 0;
    @Unique
    private static int dawnoftimeextras$page = 0;
    @Unique
    private static boolean dawnoftimeextras$hasSetItemsYet = false;
    @Unique
    private final int dawnoftimeextras$maxPage = (int) Math.floor((ExtraAdditionsCategory.values().length - 1) / 4.0);

    protected ExtraAdditionsCreativeMixin(CreativeModeInventoryScreen.ItemPickerMenu menu,
                                           Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void dawnoftimeextras$init(CallbackInfo ci) {
        this.dawnoftimeextras$buttons = new ArrayList<>();
        this.addRenderableWidget(this.dawnoftimeextras$btnScrollUp = new ExtraAdditionsGroupButton(
                this.leftPos - 22, this.topPos - 22, Component.empty(), button -> this.dawnoftimeextras$scrollUp(),
                ExtraAdditionsCategoryButton.CREATIVE_ICONS, 0, 56));
        this.addRenderableWidget(this.dawnoftimeextras$btnScrollDown = new ExtraAdditionsGroupButton(
                this.leftPos - 22, this.topPos + 120, Component.empty(), button -> this.dawnoftimeextras$scrollDown(),
                ExtraAdditionsCategoryButton.CREATIVE_ICONS, 16, 56));

        for (int i = 0; i < 4; i++) {
            this.dawnoftimeextras$buttons.add(new ExtraAdditionsCategoryButton(
                    this.leftPos - 27, this.topPos + 30 * i, i, categoryButton -> {
                if (!categoryButton.isSelected()) {
                    this.dawnoftimeextras$buttons.get(dawnoftimeextras$selectedCategoryID % 4).setSelected(false);
                    categoryButton.setSelected(true);
                    dawnoftimeextras$selectedCategoryID = categoryButton.getCategoryID();
                    dawnoftimeextras$selectedSubTab = -1;
                    this.dawnoftimeextras$updateSubTabs();
                    this.dawnoftimeextras$updateItems();
                }
            }, () -> dawnoftimeextras$page));
        }
        for (ExtraAdditionsCategoryButton button : this.dawnoftimeextras$buttons) {
            this.addRenderableWidget(button);
        }

        this.dawnoftimeextras$subTabButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int subTabIndex = i;
            ExtraAdditionsSubTabButton subTabButton = new ExtraAdditionsSubTabButton(
                    this.leftPos + 156, this.topPos + 4, ExtraAdditionsSubTab.BUILDING, button -> {
                dawnoftimeextras$selectedSubTab = dawnoftimeextras$selectedSubTab == subTabIndex ? -1 : subTabIndex;
                this.dawnoftimeextras$updateSubTabs();
                this.dawnoftimeextras$updateItems();
            });
            this.dawnoftimeextras$subTabButtons.add(subTabButton);
            this.addRenderableWidget(subTabButton);
        }
        this.dawnoftimeextras$updateSubTabs();

        this.dawnoftimeextras$updateCategoryButtons();
        if (this.dawnoftimeextras$tabSelected) {
            this.dawnoftimeextras$updateItems();
            this.dawnoftimeextras$toggleButtons(true);
            this.dawnoftimeextras$buttons.get(dawnoftimeextras$selectedCategoryID % 4).setSelected(true);
        } else {
            this.dawnoftimeextras$toggleButtons(false);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void dawnoftimeextras$render(GuiGraphics graphics, int mouseX, int mouseY,
                                          float partialTick, CallbackInfo ci) {
        if (!dawnoftimeextras$hasSetItemsYet && this.dawnoftimeextras$tabSelected) {
            this.dawnoftimeextras$updateItems();
            dawnoftimeextras$hasSetItemsYet = true;
        } else if (!this.dawnoftimeextras$tabSelected) {
            dawnoftimeextras$hasSetItemsYet = false;
        }
        this.dawnoftimeextras$toggleButtons(this.dawnoftimeextras$tabSelected);
    }

    @Inject(method = "selectTab", at = @At("HEAD"))
    private void dawnoftimeextras$selectTab(CreativeModeTab tab, CallbackInfo ci) {
        this.dawnoftimeextras$tabSelected = tab == DawnOfTimeExtras.EXTRA_ADDITIONS_TAB;
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void dawnoftimeextras$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY,
                                                 CallbackInfoReturnable<Boolean> cir) {
        if (!this.dawnoftimeextras$tabSelected) {
            return;
        }
        int startX = this.leftPos - 32;
        int startY = this.topPos + 10;
        int endY = startY + 112 + 3;
        if (mouseX >= startX && mouseX < this.leftPos && mouseY >= startY && mouseY < endY) {
            if (scrollY > 0.0) {
                this.dawnoftimeextras$scrollUp();
            } else {
                this.dawnoftimeextras$scrollDown();
            }
            cir.setReturnValue(true);
        }
    }

    @Unique
    private void dawnoftimeextras$toggleButtons(boolean val) {
        if (this.dawnoftimeextras$buttons == null) {
            return;
        }
        this.dawnoftimeextras$btnScrollUp.visible = val;
        this.dawnoftimeextras$btnScrollDown.visible = val;
        this.dawnoftimeextras$buttons.forEach(button -> button.visible = val);
        int slots = ExtraAdditionsSubTab.forCategory(
                ExtraAdditionsCategory.values()[dawnoftimeextras$selectedCategoryID]).size();
        for (int i = 0; i < this.dawnoftimeextras$subTabButtons.size(); i++) {
            this.dawnoftimeextras$subTabButtons.get(i).visible = val && i < slots;
        }
    }

    @Unique
    private void dawnoftimeextras$updateCategoryButtons() {
        this.dawnoftimeextras$btnScrollUp.active = dawnoftimeextras$page > 0;
        this.dawnoftimeextras$btnScrollDown.active = dawnoftimeextras$page < this.dawnoftimeextras$maxPage;
        this.dawnoftimeextras$buttons.forEach(button ->
                button.active = button.getCategoryID() < ExtraAdditionsCategory.values().length);
        this.dawnoftimeextras$buttons.get(dawnoftimeextras$selectedCategoryID % 4).setSelected(
                dawnoftimeextras$selectedCategoryID - dawnoftimeextras$page * 4 >= 0
                        && dawnoftimeextras$selectedCategoryID - dawnoftimeextras$page * 4 < 4);
    }

    @Unique
    private void dawnoftimeextras$scrollUp() {
        if (dawnoftimeextras$page > 0) {
            dawnoftimeextras$page--;
            this.dawnoftimeextras$updateCategoryButtons();
        }
    }

    @Unique
    private void dawnoftimeextras$scrollDown() {
        if (dawnoftimeextras$page < this.dawnoftimeextras$maxPage) {
            dawnoftimeextras$page++;
            this.dawnoftimeextras$updateCategoryButtons();
        }
    }

    @Unique
    private void dawnoftimeextras$updateSubTabs() {
        if (this.dawnoftimeextras$subTabButtons == null) {
            return;
        }
        List<ExtraAdditionsSubTab> sets = ExtraAdditionsSubTab.forCategory(
                ExtraAdditionsCategory.values()[dawnoftimeextras$selectedCategoryID]);
        for (int i = 0; i < this.dawnoftimeextras$subTabButtons.size() && i < sets.size(); i++) {
            ExtraAdditionsSubTabButton button = this.dawnoftimeextras$subTabButtons.get(i);
            button.setSubTab(sets.get(i));
            button.setX(this.leftPos + 156 - (sets.size() - 1 - i) * 14);
            button.setSelected(i == dawnoftimeextras$selectedSubTab);
        }
    }

    @Unique
    private void dawnoftimeextras$updateItems() {
        ExtraAdditionsCategory category = ExtraAdditionsCategory.values()[dawnoftimeextras$selectedCategoryID];
        this.menu.items.clear();
        List<ExtraAdditionsSubTab> sets = ExtraAdditionsSubTab.forCategory(category);
        ExtraAdditionsSubTab subTab = dawnoftimeextras$selectedSubTab >= 0 && dawnoftimeextras$selectedSubTab < sets.size()
                ? sets.get(dawnoftimeextras$selectedSubTab) : null;
        for (Block block : DawnOfTimeExtras.CATEGORY_BLOCKS.get(category)) {
            if (subTab == null || subTab.contains(block)) {
                this.menu.items.add(new ItemStack(block));
            }
        }
        this.menu.scrollTo(0.0F);
    }
}
