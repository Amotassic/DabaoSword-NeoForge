package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class FangtianWeapon extends EquipmentItem {
    public FangtianWeapon(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            int cd = ModTools.getCD(stack);
            if (cd > 0 && world.getGameTime() % 20 == 0) cd--; stack.set(ModItems.CD, cd);
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && !SkillItem.equipped(stack)) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.literal(slotContext.entity().getScoreboardName()).append(Component.literal(" equipped ").append(stack.getDisplayName())),false
            ));
            SkillItem.setEquipped(stack, true);
        }
    }
}
