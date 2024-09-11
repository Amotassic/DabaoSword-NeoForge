package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.*;

public class GuoseSkill extends SkillItem {
    public GuoseSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide && entity instanceof Player player && noTieji(entity)) {
            ItemStack stack1 = player.getOffhandItem();
            int cd = getCD(stack);
            if (cd == 0 && !stack1.isEmpty() && stack1.getItem() == ModItems.SHAN.get()) {
                setCD(stack, 15);
                stack1.shrink(1);
                give(player, ModItems.TOO_HAPPY_ITEM.get().getDefaultInstance());
                voice(player, Sounds.GUOSE.get());
            }
        }
        super.curioTick(slotContext, stack);
    }
}
