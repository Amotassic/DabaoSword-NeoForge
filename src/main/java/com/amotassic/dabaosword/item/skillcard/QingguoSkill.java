package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class QingguoSkill extends SkillItem {
    public QingguoSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide && entity instanceof Player player && noTieji(entity)) {
            ItemStack stack1 = player.getOffhandItem();
            int cd = getCD(stack);
            if (cd == 0 && !stack1.isEmpty() && nonBasic(stack1)) {
                setCD(stack, 5);
                stack1.shrink(1);
                give(player, ModItems.SHAN.get().getDefaultInstance());
                if (new Random().nextFloat() < 0.5) {voice(player, Sounds.QINGGUO1.get());} else {voice(player, Sounds.QINGGUO2.get());}
            }
        }
        super.curioTick(slotContext, stack);
    }
}
