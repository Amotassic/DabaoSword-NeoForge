package com.amotassic.dabaosword.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class PileHandledScreen extends AbstractContainerScreen<PileScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");

    public PileHandledScreen(PileScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 114 + 4 * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, 4 * 18 + 17);
        guiGraphics.blit(TEXTURE, i, j + 4 * 18 + 17, 0, 126, this.imageWidth, 96);
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
