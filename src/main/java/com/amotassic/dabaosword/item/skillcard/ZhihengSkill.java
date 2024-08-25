package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.getTag;

public class ZhihengSkill extends SkillItem {
    public ZhihengSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            int z = getTag(stack);
            if (z < 10) {
                if (world.getGameTime() % 100 == 0) {z++; stack.set(ModItems.TAGS, z);}
            }
        }
        super.curioTick(slotContext, stack);
    }
}
