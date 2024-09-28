package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.amotassic.dabaosword.util.ModTools.*;

public class ShanItem extends CardItem {
    public ShanItem(Properties p_41383_) {super(p_41383_);}

    //使用后，向前冲刺一段距离，无敌0.5秒，冷却时间1秒
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        //判断是否有独立冷却buff，若冷却中则无法生效
        if (!world.isClientSide && !user.hasEffect(ModItems.COOLDOWN2) && hand == InteractionHand.MAIN_HAND) {
            Vec3 momentum = user.getForward().scale(3);
            user.hurtMarked = true; user.setDeltaMovement(momentum.x,0 ,momentum.z);
            int i = hasTrinket(SkillCards.LEIJI, user) ? 3 : 0;
            user.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
            user.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 20,i,false,false,false));
            cardUsePost(user, user.getItemInHand(hand), user);
            voice(user, Sounds.SHAN);
        }
        return  InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
