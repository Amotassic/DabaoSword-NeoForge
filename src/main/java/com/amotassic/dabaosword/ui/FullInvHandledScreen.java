package com.amotassic.dabaosword.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import java.util.Set;

public class FullInvHandledScreen extends AbstractContainerScreen<FullInvScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private final Set<Integer> slotsEnabled;
    private final int rows;
    private final boolean notSelf;
    private final int armorRow;

    public FullInvHandledScreen(FullInvScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.slotsEnabled = handler.slotsEnabled;
        this.rows = handler.rows;
        this.notSelf = handler.notSelf;
        this.armorRow = handler.armorRow;
        int height = 38 + rows * 18; if (notSelf) height += 76;
        this.imageHeight = height;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v0, int i0, int i1) {
        int x = this.leftPos; int y = this.topPos;
        guiGraphics.blit(TEXTURE, x, y,0,0, imageWidth, 17);
        for (int i = 0; i < rows * 2; i++) { //填充空白背景
            guiGraphics.blit(TEXTURE, x, y + 17 + i * 9,0,125, imageWidth, 9);
        }
        int v = notSelf ? 125 : 215; int height = notSelf ? 97 : 7;
        guiGraphics.blit(TEXTURE, x, y + rows * 18 + 17,0, v, imageWidth, height);
        for (int i : slotsEnabled) { //绘制启用的格子背景
            Slot slot = menu.getSlot(i);
            guiGraphics.blit(TEXTURE, slot.x + x - 1, slot.y + y - 1,7, 17, 18, 18);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        if (notSelf) guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 21 + rows * 18, 0x404040, false);
        if (slotsEnabled.contains(41)) guiGraphics.drawString(this.font, Component.translatable("trinkets"), 8, 5 + armorRow * 18, 0x404040, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (minecraft != null && minecraft.gameMode != null && hoveredSlot!= null && minecraft.player != null && keyCode == 261) {
            minecraft.gameMode.handleInventoryMouseClick(menu.containerId, hoveredSlot.index, 114, ClickType.THROW, minecraft.player);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
