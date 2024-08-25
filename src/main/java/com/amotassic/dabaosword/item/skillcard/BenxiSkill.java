package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.*;

public class BenxiSkill extends SkillItem {
    public BenxiSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!slotContext.entity().level().isClientSide && slotContext.entity() instanceof Player player && noLongHand(player) && noTieji(slotContext.entity())) {
            int benxi = getTag(stack);
            if (hasTrinket(ModItems.CHITU.get(), player) && hasTrinket(SkillCards.MASHU.get(), player)) {
                player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi + 2,false,false,true));
            } else if (hasTrinket(ModItems.CHITU.get(), player) || hasTrinket(SkillCards.MASHU.get(), player)) {
                player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi + 1,false,false,true));
            } else if (benxi != 0) {
                player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi - 1,false,false,true));
            }
        }
        super.curioTick(slotContext, stack);
    }

    private boolean noLongHand(Player player) {
        return player.getMainHandItem().getItem() != ModItems.JUEDOU.get() && player.getMainHandItem().getItem() != ModItems.DISCARD.get();
    }
}
