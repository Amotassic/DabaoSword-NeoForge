package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.amotassic.dabaosword.util.ModTools.getOrCreateNbt;
import static com.amotassic.dabaosword.util.ModTools.s;

public class PlayerInvHandledScreen extends AbstractContainerScreen<PlayerInvScreenHandler> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dabaosword", "textures/gui/generic_54.png");

    public PlayerInvHandledScreen(PlayerInvScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight = 130;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = this.leftPos; int y = this.topPos;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        PoseStack matrices = guiGraphics.pose();
        matrices.pushPose();
        matrices.translate(0f, 0f, 400f);
        getClicks().forEach((i, clicks) -> {
            Slot slot = menu.getSlot(i);
            guiGraphics.drawString(this.font, Component.literal(clicks + "").withStyle(ChatFormatting.RED), slot.x, slot.y + 6, 0x404040, false);
        });
        matrices.popPose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        //System.out.println("keyCode: " + keyCode + " scanCode: " + scanCode + " modifiers: " + modifiers);
        boolean ctrlA = keyCode == 65 && modifiers == 2;
        boolean ctrlZ = keyCode == 90 && modifiers == 2;
        assert minecraft != null; var stack = eventStack();
        var manager = minecraft.gameMode; var player = minecraft.player;
        if (manager != null && player!= null) {
            if (ctrlA) {manager.handleInventoryMouseClick(menu.containerId, 0, 65, ClickType.PICKUP_ALL, player); return true;}
            if (ctrlZ) {manager.handleInventoryMouseClick(menu.containerId, 0, 90, ClickType.PICKUP_ALL, player); return true;}
        }
        boolean canClose = !(stack.getItem() instanceof ISkill) || selectedCount() >= s(stack).getMinSelect();
        if (!canClose || stack.is(ModItems.DISCARD) || stack.is(ModItems.STEAL)) {
            if (minecraft.options.keyInventory.matches(keyCode, scanCode) || keyCode == 256) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double amount) {
        if (minecraft == null) return false;
        var manager = minecraft.gameMode; var player = minecraft.player;
        if (manager != null && hoveredSlot != null && player!= null) {
            int id = hoveredSlot.index; //仅用于根据鼠标滚轮滚动来选择卡牌
            if (amount == 1.0) manager.handleInventoryMouseClick(menu.containerId, id, 0, ClickType.PICKUP, player);
            if (amount == -1.0) manager.handleInventoryMouseClick(menu.containerId, id, 1, ClickType.PICKUP, player);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, amount);
    }

    private ItemStack eventStack() {return menu.getSlot(55).getItem();}

    private Map<Integer, Integer> getClicks() {
        String str = getOrCreateNbt(menu.getSlot(57).getItem()).getString("Clicks");
        Map<Integer, Integer> clicks = new HashMap<>();
        if (str.isEmpty()) return clicks;
        str = str.substring(1, str.length() - 1); //去掉{}如果还是空，则返回空map
        if (str.isEmpty()) return clicks;

        String[] split = str.split(", ");
        for (String s : split) {
            String[] split1 = s.split("=");
            clicks.put(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]));
        }
        return clicks;
    }
    private int selectedCount() {return getClicks().values().stream().mapToInt(Integer::intValue).sum();}
}
